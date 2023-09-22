package com.banquemisr.currency.ui.network

import com.banquemisr.currency.ui.ui.main.home.LatestCurrenciesResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsResponse
import retrofit2.http.*

interface MoviesWebServices {

    companion object {
        private const val SYMBOLS = "symbols"
        private const val LATEST = "latest"
        private const val CONVERT = "convert"
    }

    @GET(SYMBOLS)
    suspend fun getSymbols(@QueryMap param: HashMap<String, String?>): SymbolsResponse

    @GET(LATEST)
    suspend fun getLatestTransactions(@QueryMap param: HashMap<String, String?>): LatestCurrenciesResponse

    @GET(CONVERT)
    suspend fun convertAmount(@QueryMap param: HashMap<String, String?>): ConvertResponse
}