package com.banquemisr.currency.ui.data

import com.banquemisr.currency.ui.extesnion.toHashMapParams
import javax.inject.Inject

class RemoteDataSourceRepoImpl @Inject constructor(private val apiInterface : MoviesWebServices) :
    IMoviesIRemoteDataSourceRepo {

    override suspend fun getLatest(params: LatestParams): ExchangeRates {
        return apiInterface.getLatestTransactions(params.toHashMapParams()!!)
    }

    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return apiInterface.getSymbols(params.toHashMapParams()!!)
    }

    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return apiInterface.convertAmount(params.toHashMapParams()!!)
    }
}