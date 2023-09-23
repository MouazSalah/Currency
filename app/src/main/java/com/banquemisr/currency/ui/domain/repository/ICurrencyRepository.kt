package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.convert.ConvertParams
import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import com.banquemisr.currency.ui.network.ApiResult

interface ICurrencyRepository {

    suspend fun getExchangeRatesFromApi(params: ExchangeRatesParams): ApiResult<ExchangeRatesUIModel>

    suspend fun getExchangeRatesFromRoom(): ExchangeRatesUIModel?

    suspend fun insertExchangeRatesToRoom(exchangeRates: ExchangeRatesApiModel)


    suspend fun getSymbols(params : SymbolsParams): SymbolsResponse

    suspend fun convertAmount(params : ConvertParams): ConvertResponse
}
