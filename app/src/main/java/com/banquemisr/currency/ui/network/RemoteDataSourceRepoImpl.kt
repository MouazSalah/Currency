package com.banquemisr.currency.ui.network

import com.banquemisr.currency.ui.extesnion.toHashMapParams
import com.banquemisr.currency.ui.ui.main.home.LatestCurrenciesResponse
import com.banquemisr.currency.ui.ui.main.home.LatestParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsResponse
import javax.inject.Inject

class RemoteDataSourceRepoImpl @Inject constructor(private val apiInterface : MoviesWebServices) :
    IMoviesIRemoteDataSourceRepo {

    override suspend fun getLatest(params: LatestParams): LatestCurrenciesResponse {
        return apiInterface.getLatestTransactions(params.toHashMapParams()!!)
    }

    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return apiInterface.getSymbols(params.toHashMapParams()!!)
    }

    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return apiInterface.convertAmount(params.toHashMapParams()!!)
    }
}