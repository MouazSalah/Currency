package com.banquemisr.currency.ui.data.api

import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import retrofit2.Response
import retrofit2.http.*

interface CurrencyWebServices {

    companion object {
        private const val SYMBOLS = "symbols"
        private const val LATEST = "latest"
        private const val CONVERT = "convert"
    }

    // http://data.fixer.io/api/2023-09-23?access_key=8be949615443b9c187f9f908d97f43b7

    @GET(LATEST)
    suspend fun getExchangeRates(@QueryMap param: HashMap<String, String?>): Response<ExchangeRatesApiModel>


    @GET(LATEST)
    suspend fun getHistoricalRates(@QueryMap param: HashMap<String, String?>): Response<HistoryRateResponse>

    @GET(SYMBOLS)
    suspend fun getSymbols(@QueryMap param: HashMap<String, String?>): SymbolsResponse

    @GET(CONVERT)
    suspend fun convertAmount(@QueryMap param: HashMap<String, String?>): ConvertResponse
}