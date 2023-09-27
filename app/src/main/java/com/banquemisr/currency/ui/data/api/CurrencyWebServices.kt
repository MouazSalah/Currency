package com.banquemisr.currency.ui.data.api

import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import retrofit2.Response
import retrofit2.http.*

interface CurrencyWebServices {

    companion object {
        private const val SYMBOLS = "symbols"
        private const val LATEST = "latest"
    }

    @GET(SYMBOLS)
    suspend fun getSymbols(@QueryMap param: HashMap<String, String?>): SymbolsResponse

    @GET(LATEST)
    suspend fun getExchangeRates(@QueryMap param: HashMap<String, String?>): Response<ExchangeRatesApiModel>


    @GET("{date}")
    suspend fun getHistoricalRates(@Path("date") date : String, @QueryMap params: HashMap<String, String?>): Response<HistoryRateResponse>

}