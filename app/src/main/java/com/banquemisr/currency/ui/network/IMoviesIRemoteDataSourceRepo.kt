package com.banquemisr.currency.ui.network

import com.banquemisr.currency.ui.ui.main.home.LatestCurrenciesResponse
import com.banquemisr.currency.ui.ui.main.home.LatestParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsResponse
import retrofit2.http.QueryMap

interface IMoviesIRemoteDataSourceRepo {

    suspend fun getLatest(params : LatestParams) : LatestCurrenciesResponse

    suspend fun getSymbols(params : SymbolsParams): SymbolsResponse

    suspend fun convertAmount(params : ConvertParams): ConvertResponse
}