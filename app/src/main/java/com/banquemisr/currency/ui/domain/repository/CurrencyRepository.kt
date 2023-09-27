package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.core.BaseApp
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.datasource.ICurrencyLocalDataSourceRepo
import com.banquemisr.currency.ui.data.api.ICurrencyRemoteDataSourceRepo
import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.data.model.symbols.Symbols
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.domain.mapper.ExchangeRatesMapper
import com.banquemisr.currency.ui.extesnion.isNetworkAvailable
import com.banquemisr.currency.ui.network.ApiResult
import java.util.Locale
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSourceRepo: ICurrencyLocalDataSourceRepo,
    private val remoteDataSource: ICurrencyRemoteDataSourceRepo
) : ICurrencyRepository {

    override suspend fun getExchangeRatesFromApi(params: ExchangeRatesParams): ApiResult<ExchangeRatesUIModel> {
        return if (isNetworkAvailable(BaseApp.instance.applicationContext)) {
            try {
                val response = remoteDataSource.getExchangeRates(params)

                if (response.isSuccessful) {
                    response.body()?.let { exchangeRatesResponse ->
                        insertExchangeRatesToRoom(exchangeRatesResponse)
                        val exchangeRateUIModel = ExchangeRatesMapper.mapApiModelToUIModel(exchangeRatesResponse)
                        ApiResult.Success(exchangeRateUIModel)
                    } ?: run {
                        getExchangeRatesFromRoom()?.let {
                            ApiResult.CashedData(it)
                        } ?: ApiResult.ApiError("${response.code()} - ${response.message()}")
                    }
                } else {
                    getExchangeRatesFromRoom()?.let {
                        ApiResult.CashedData(it)
                    } ?: ApiResult.ApiError("${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                getExchangeRatesFromRoom()?.let {
                    ApiResult.CashedData(it)
                } ?: ApiResult.ApiError(e.message.toString())

                // Handle additional error cases if needed
            }
        } else {
            getExchangeRatesFromRoom()?.let {
                ApiResult.CashedData(it)
            } ?: ApiResult.InternetError("No internet connection")
        }
    }

    override suspend fun getHistoricalRates(params: HistoricalRatesParams): ApiResult<HistoryRateResponse> {
        return if (isNetworkAvailable(BaseApp.instance.applicationContext)) {
            try {
                val response = remoteDataSource.getHistoricalRates(params)
                if (response.isSuccessful) {
                    response.body()?.let { historyRateResponse ->
                        ApiResult.Success(historyRateResponse)
                    } ?: run {
                        ApiResult.ApiError("${response.code()} - ${response.message()}")
                    }
                } else {
                    ApiResult.ApiError("${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                ApiResult.ApiError(e.message.toString())
            }
        } else {
            ApiResult.InternetError("No internet connection")
        }
    }

    override suspend fun insertExchangeRatesToRoom(exchangeRates: ExchangeRatesApiModel) {
        val exchangeRatesEntity: ExchangeRatesEntity =
            ExchangeRatesMapper.mapApiModelToRoomModel(exchangeRates)
        localDataSourceRepo.insertExchangeRates(exchangeRatesEntity)
    }

    override suspend fun getExchangeRatesFromRoom(): ExchangeRatesUIModel? {
        val exchangeRatesRoomModel: ExchangeRatesEntity? = localDataSourceRepo.loadExchangeRates()
        return if (exchangeRatesRoomModel == null)
            null
        else
            ExchangeRatesMapper.mapRoomModelToUIModel(exchangeRatesRoomModel)
    }


    override suspend fun getSymbols(params: SymbolsParams): List<String> {
        val symbolsResponse = remoteDataSource.getSymbols(params)
        return getCurrencySymbols(symbolsResponse.symbols ?: Symbols())
    }

    private fun getCurrencySymbols(symbols: Symbols): List<String> {
        var currencySymbols = mutableListOf<String>()

        currencySymbols = symbols.javaClass.declaredFields
            .map { it.name }
            .map { it.substring(0, 3) }
            .map { it.uppercase(Locale.ROOT) }.toMutableList()

        return currencySymbols
    }
}
