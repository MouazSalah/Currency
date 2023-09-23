package com.banquemisr.currency.ui.data.api

import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface CurrencyWebServices {

    companion object {
        private const val SYMBOLS = "symbols"
        private const val LATEST = "latest"
        private const val CONVERT = "convert"
    }

    @GET(LATEST)
    suspend fun getExchangeRates(@QueryMap param: HashMap<String, String?>): Flow<ExchangeRatesApiModel>

    @GET(SYMBOLS)
    suspend fun getSymbols(@QueryMap param: HashMap<String, String?>): SymbolsResponse

    @GET(CONVERT)
    suspend fun convertAmount(@QueryMap param: HashMap<String, String?>): ConvertResponse
}