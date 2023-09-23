package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.core.BaseApp
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.convert.ConvertParams
import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.datasource.ICurrencyLocalDataSourceRepo
import com.banquemisr.currency.ui.data.api.ICurrencyRemoteDataSourceRepo
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import com.banquemisr.currency.ui.domain.mapper.ExchangeRatesMapper
import com.banquemisr.currency.ui.extesnion.isNetworkAvailable
import com.banquemisr.currency.ui.extesnion.showLogMessage
import com.banquemisr.currency.ui.network.ApiResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSourceRepo: ICurrencyLocalDataSourceRepo,
    private val remoteDataSource: ICurrencyRemoteDataSourceRepo
) : ICurrencyRepository {


    override suspend fun getExchangeRatesFromApi(params: ExchangeRatesParams): ApiResult<ExchangeRatesUIModel> {
        return if (isNetworkAvailable(BaseApp.instance.applicationContext)) {
            try {
                "collect method before calling ".showLogMessage()

                GlobalScope.launch {

                    "collect method before calling  GlobalScope ".showLogMessage()

                    remoteDataSource.getExchangeRates(params).collect{ value ->
                        "collect method is called ".showLogMessage()
                        "collect method is called ${value.toString()} ".showLogMessage()
                    }
                }
                "collect method before calling ".showLogMessage()

                ApiResult.ApiError(Exception("Mapping error"))

//                var exchangeRateUIModel: ExchangeRatesUIModel? = null
//
//                response.collect { exchangeRates ->
//
//                    "collect method is called".showLogMessage()
//
//                    insertExchangeRatesToRoom(exchangeRates)
//                    exchangeRateUIModel = ExchangeRatesMapper.mapApiModelToUIModel(exchangeRates)
//                }
//
//                if (exchangeRateUIModel != null) {
//
//                    "api called successfully".showLogMessage()
//
//                    ApiResult.Success(exchangeRateUIModel!!)
//                } else {
//
//                    if (getExchangeRatesFromRoom() != null)
//                    {
//                        "room data is NOOOOT null".showLogMessage()
//                    }
//                    else {
//                        "room data is null".showLogMessage()
//                    }
//
//                    ApiResult.ApiError(Exception("Mapping error"))
//                }

            } catch (e: Exception) {
                "catch exception ${e.message.toString()}".showLogMessage()

                if (getExchangeRatesFromRoom() != null)
                {
                    "catch exception room data is NOOOOT null".showLogMessage()
                }
                else {
                    "catch exception  room data is null".showLogMessage()
                }
                ApiResult.ApiError(e)
            }
        } else {
            ApiResult.InternetError(Exception("No internet connection")) // Custom exception for no internet
        }
    }

    override suspend fun insertExchangeRatesToRoom(exchangeRates: ExchangeRatesApiModel) {
        "insertExchangeRatesToRoom before ".showLogMessage()
        val exchangeRatesEntity : ExchangeRatesEntity = ExchangeRatesMapper.mapApiModelToRoomModel(exchangeRates)
        "insertExchangeRatesToRoom exchangeRatesEntity = $exchangeRatesEntity".showLogMessage()
        localDataSourceRepo.insertExchangeRates(exchangeRatesEntity)
    }

    override suspend fun getExchangeRatesFromRoom(): ExchangeRatesUIModel? {
        val exchangeRatesRoomModel : ExchangeRatesEntity? = localDataSourceRepo.loadExchangeRates()
        return if (exchangeRatesRoomModel == null)
            null
        else
            ExchangeRatesMapper.mapRoomModelToUIModel(exchangeRatesRoomModel)
    }


    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return remoteDataSource.getSymbols(params)
    }

    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return remoteDataSource.convertAmount(params)
    }

}
