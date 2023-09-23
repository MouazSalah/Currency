package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.core.BaseApp
import com.banquemisr.currency.ui.data.ExchangeRates
import com.banquemisr.currency.ui.data.LatestParams
import com.banquemisr.currency.ui.data.ConvertParams
import com.banquemisr.currency.ui.data.ConvertResponse
import com.banquemisr.currency.ui.data.ICurrencyLocalDataSourceRepo
import com.banquemisr.currency.ui.data.ICurrencyRemoteDataSourceRepo
import com.banquemisr.currency.ui.data.SymbolsParams
import com.banquemisr.currency.ui.data.SymbolsResponse
import com.banquemisr.currency.ui.extesnion.isNetworkAvailable
import com.banquemisr.currency.ui.network.ApiResult
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val localDataSourceRepo: ICurrencyLocalDataSourceRepo,
    private val remoteDataSource: ICurrencyRemoteDataSourceRepo
) : ICurrencyRepository {

    override suspend fun getExchangeRates(params: LatestParams): ApiResult<ExchangeRates> {
        return if (isNetworkAvailable(BaseApp.instance.applicationContext)) {
            try {

               // val response = remoteDataSource.get(params)

                ApiResult.Success(ExchangeRates(base = "", date = "", rates = mapOf()))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        } else {
            ApiResult.Error(Exception("No internet connection")) // Custom exception for no internet
        }
    }

//    override suspend fun getExchangeRatesFlow(params: LatestParams): Flow<ExchangeRates> {
//        return remoteDataSource.getExchangeRates(params)
//    }

//    override suspend fun getExchangeRates(params: LatestParams): ExchangeRates {
//
//        var exchangeRates : ExchangeRates
//
//        if (isNetworkAvailable(BaseApp.instance.applicationContext)) {
//
//            remoteDataSource.getLatest(params).collect{ result ->
//                when (result) {
//                    is ApiResult.Success -> {
//                        exchangeRates = result.data
//
//                        localDataSourceRepo.insertExchangeRates(ExchangeRatesEntity(
//                            base = exchangeRates.base,
//                            date = exchangeRates.date,
//                            rates = exchangeRates.rates
//                        ))
//                    }
//                    is ApiResult.Error -> {
//                        exchangeRates = result.data
//                    }
//                }
//            }
//        }
//        else {
//
//        }
//
//        remoteDataSource.getLatest(params).collect{ result ->
//            when (result) {
//                is ApiResult.Success -> {
//                    exchangeRates = result.data
//
//                    localDataSourceRepo.insertExchangeRates(ExchangeRatesEntity(
//                        base = exchangeRates.base,
//                        date = exchangeRates.date,
//                        rates = exchangeRates.rates
//                    ))
//                }
//                is ApiResult.Error -> {
//                    exchangeRates = result.data
//                }
//            }
//        }
//
//        return remoteDataSource.getLatest(params)
//    }

    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return remoteDataSource.getSymbols(params)
    }

    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return remoteDataSource.convertAmount(params)
    }

}
