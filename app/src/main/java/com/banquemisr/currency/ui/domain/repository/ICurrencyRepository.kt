package com.banquemisr.currency.ui.domain.repository

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.convert.ConvertParams
import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.db.CurrencyEntity
import com.banquemisr.currency.ui.network.ApiResult

interface ICurrencyRepository {

    suspend fun getExchangeRatesFromApi(params: ExchangeRatesParams): ApiResult<ExchangeRatesUIModel>

    suspend fun getHistoricalRates(params: HistoricalRatesParams): ApiResult<HistoryRateResponse>

    suspend fun getExchangeRatesFromRoom(): ExchangeRatesUIModel?

    suspend fun insertExchangeRatesToRoom(exchangeRates: ExchangeRatesApiModel)


    suspend fun getSymbols(params : SymbolsParams): List<String>

    suspend fun convertAmount(params : ConvertParams): ConvertResponse

//    suspend fun getCurrencies(): ApiResult<CurrencyEntity>
//    suspend fun insertCurrencies(currencies : CurrencyEntity)
}
