package com.banquemisr.currency.ui.data.api

import com.banquemisr.currency.ui.data.model.convert.ConvertParams
import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ICurrencyRemoteDataSourceRepo {

    suspend fun getExchangeRates(params : ExchangeRatesParams) : Response<ExchangeRatesApiModel>

    suspend fun getSymbols(params : SymbolsParams): SymbolsResponse

    suspend fun convertAmount(params : ConvertParams): ConvertResponse
}