package com.banquemisr.currency.ui.data

import com.banquemisr.currency.ui.extesnion.toHashMapParams
import com.banquemisr.currency.ui.network.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRemoteDataSourceRepoImpl @Inject constructor(private val apiInterface : CurrencyWebServices) :
    ICurrencyRemoteDataSourceRepo {

    override suspend fun getExchangeRates(params: LatestParams): Flow<ExchangeRates> {
        return apiInterface.getExchangeRates(params.toHashMapParams()!!)
    }

    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return apiInterface.getSymbols(params.toHashMapParams()!!)
    }

    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return apiInterface.convertAmount(params.toHashMapParams()!!)
    }
}