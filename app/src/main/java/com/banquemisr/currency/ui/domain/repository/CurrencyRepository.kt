package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.core.BaseApp
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.convert.ConvertParams
import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.datasource.ICurrencyLocalDataSourceRepo
import com.banquemisr.currency.ui.data.api.ICurrencyRemoteDataSourceRepo
import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.data.model.symbols.Symbols
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
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
        TODO("Not yet implemented")
    }

//    override suspend fun getExchangeRatesFromApi(params: ExchangeRatesParams): ApiResult<ExchangeRatesUIModel> {
//        return if (isNetworkAvailable(BaseApp.instance.applicationContext))
//        {
//            try
//            {
//                val response = remoteDataSource.getExchangeRates(params)
//
//                if (response.isSuccessful) {
//
//                    val exchangeRatesResponse = response.body()
//
//                    if (exchangeRatesResponse != null) {
//                        insertExchangeRatesToRoom(exchangeRatesResponse)
//                        val exchangeRateUIModel = ExchangeRatesMapper.mapApiModelToUIModel(exchangeRatesResponse)
//                        ApiResult.Success(exchangeRateUIModel)
//                    } else {
//                        val exchangeRateUIModel: ExchangeRatesUIModel? = getExchangeRatesFromRoom()
//                        if (exchangeRateUIModel != null) {
//                            ApiResult.CashedData(exchangeRateUIModel)
//                        } else {
//                            ApiResult.ApiError("${response.code()} - ${response.message()}")
//                        }
//                    }
//                } else {
//                    // Non-successful HTTP response
//                    val exchangeRateUIModel: ExchangeRatesUIModel? =
//                        getExchangeRatesFromRoom() // Assuming getExchangeRatesFromRoom() returns non-null
//                    if (exchangeRateUIModel != null) {
//                        ApiResult.CashedData(exchangeRateUIModel)
//                    } else {
//                        ApiResult.ApiError("${response.code()} - ${response.message()}")
//                    }
//                }
//            }
//            catch (e: Exception)
//            {
//
//                val exchangeRateUIModel: ExchangeRatesUIModel? =
//                    getExchangeRatesFromRoom() // Assuming getExchangeRatesFromRoom() returns non-null
//                if (exchangeRateUIModel != null) {
//                    ApiResult.CashedData(exchangeRateUIModel)
//                } else {
//                    ApiResult.ApiError(e.message.toString())
//                }
//
//                //                    val errorJson = response.errorBody()?.string()
////                    val gson = Gson()
////                    val errorResponse = gson.fromJson(errorJson, ErrorResponse::class.java)
////
////                    val errorCode = errorResponse.error.code
////                    val errorInfo = errorResponse.error.info
////                    "response errorCode = ${errorCode.toString()}".showLogMessage()
////                    "response errorInfo = ${errorInfo.toString()}".showLogMessage()
//            }
//        } else {
//
//            val exchangeRateUIModel: ExchangeRatesUIModel? = getExchangeRatesFromRoom()
//            if (exchangeRateUIModel != null) {
//                ApiResult.CashedData(exchangeRateUIModel)
//            } else {
//                ApiResult.InternetError("No internet connection")
//            }
//        }
//    }

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
            .map { it.toUpperCase() }.toMutableList()

//        for (field in fields) {
//            field.isAccessible = true
//            val symbol = field.get(symbols) as? String
//            symbol?.let {
//                currencySymbols.add(it.uppercase(Locale.ROOT))
//            }
//        }

        return currencySymbols
    }


    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return remoteDataSource.convertAmount(params)
    }

}
