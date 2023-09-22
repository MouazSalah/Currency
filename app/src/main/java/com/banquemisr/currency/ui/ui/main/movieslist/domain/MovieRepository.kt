package com.banquemisr.currency.ui.ui.main.movieslist.domain

import com.banquemisr.currency.ui.network.RemoteDataSourceRepoImpl
import com.banquemisr.currency.ui.ui.main.home.LatestCurrenciesResponse
import com.banquemisr.currency.ui.ui.main.home.LatestParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsResponse
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceRepoImpl
) : IMovieRepository {

    override suspend fun getLatest(params: LatestParams): LatestCurrenciesResponse {
        return remoteDataSource.getLatest(params)
    }

    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return remoteDataSource.getSymbols(params)
    }

    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return remoteDataSource.convertAmount(params)
    }

}
