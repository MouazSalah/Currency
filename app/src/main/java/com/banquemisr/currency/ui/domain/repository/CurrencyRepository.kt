package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.data.RemoteDataSourceRepoImpl
import com.banquemisr.currency.ui.data.ExchangeRates
import com.banquemisr.currency.ui.data.LatestParams
import com.banquemisr.currency.ui.data.ConvertParams
import com.banquemisr.currency.ui.data.ConvertResponse
import com.banquemisr.currency.ui.data.SymbolsParams
import com.banquemisr.currency.ui.data.SymbolsResponse
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceRepoImpl
) : ICurrencyRepository {

    override suspend fun getLatest(params: LatestParams): ExchangeRates {
        return remoteDataSource.getLatest(params)
    }

    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return remoteDataSource.getSymbols(params)
    }

    override suspend fun convertAmount(params: ConvertParams): ConvertResponse {
        return remoteDataSource.convertAmount(params)
    }

}
