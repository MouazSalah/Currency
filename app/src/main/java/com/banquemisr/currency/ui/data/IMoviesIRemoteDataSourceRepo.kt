package com.banquemisr.currency.ui.data

import com.banquemisr.currency.ui.data.ExchangeRates
import com.banquemisr.currency.ui.data.LatestParams
import com.banquemisr.currency.ui.data.ConvertParams
import com.banquemisr.currency.ui.data.ConvertResponse
import com.banquemisr.currency.ui.data.SymbolsParams
import com.banquemisr.currency.ui.data.SymbolsResponse

interface IMoviesIRemoteDataSourceRepo {

    suspend fun getLatest(params : LatestParams) : ExchangeRates

    suspend fun getSymbols(params : SymbolsParams): SymbolsResponse

    suspend fun convertAmount(params : ConvertParams): ConvertResponse
}