package com.banquemisr.currency.ui.data

import com.banquemisr.currency.ui.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface ICurrencyRemoteDataSourceRepo {

    suspend fun getExchangeRates(params : LatestParams) : Flow<ExchangeRates>

    suspend fun getSymbols(params : SymbolsParams): SymbolsResponse

    suspend fun convertAmount(params : ConvertParams): ConvertResponse
}