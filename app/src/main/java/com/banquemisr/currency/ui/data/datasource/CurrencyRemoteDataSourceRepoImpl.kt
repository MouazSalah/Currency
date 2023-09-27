package com.banquemisr.currency.ui.data.datasource

import com.banquemisr.currency.ui.data.api.CurrencyWebServices
import com.banquemisr.currency.ui.data.api.ICurrencyRemoteDataSourceRepo
import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import com.banquemisr.currency.ui.extesnion.toHashMapParams
import retrofit2.Response
import javax.inject.Inject

class CurrencyRemoteDataSourceRepoImpl @Inject constructor(private val apiInterface : CurrencyWebServices) :
    ICurrencyRemoteDataSourceRepo {

    override suspend fun getExchangeRates(params: ExchangeRatesParams): Response<ExchangeRatesApiModel> {
        return apiInterface.getExchangeRates(params.toHashMapParams()!!)
    }

    override suspend fun getHistoricalRates(params: HistoricalRatesParams): Response<HistoryRateResponse> {
        val date = params.date.toString()
        params.date = null
        val result = apiInterface.getHistoricalRates(date = date, params = params.toHashMapParams()!!)
        return result
    }

    override suspend fun getSymbols(params: SymbolsParams): SymbolsResponse {
        return apiInterface.getSymbols(params.toHashMapParams()!!)
    }
}