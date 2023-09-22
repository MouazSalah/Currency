package com.banquemisr.currency.ui.ui.main.movieslist.domain

import com.banquemisr.currency.ui.ui.main.home.LatestCurrenciesResponse
import com.banquemisr.currency.ui.ui.main.home.LatestParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.MoviesAPIResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.MoviesListParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsResponse

interface IMovieRepository {

    suspend fun getLatest(params : LatestParams): LatestCurrenciesResponse

    suspend fun getSymbols(params : SymbolsParams): SymbolsResponse

    suspend fun convertAmount(params : ConvertParams): ConvertResponse
}
