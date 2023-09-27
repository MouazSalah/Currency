package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.di.ApiResult

interface ICurrencyRepository {

    suspend fun getExchangeRatesFromApi(params: ExchangeRatesParams): ApiResult<ExchangeRatesUIModel>

    suspend fun getHistoricalRates(params: HistoricalRatesParams): ApiResult<HistoryRateResponse>

    suspend fun getExchangeRatesFromRoom(): ExchangeRatesUIModel?

    suspend fun insertExchangeRatesToRoom(exchangeRates: ExchangeRatesApiModel)


    suspend fun getSymbols(params : SymbolsParams): List<String>
}
