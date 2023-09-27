package com.banquemisr.currency.ui.data.datasource

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity
import com.banquemisr.currency.ui.db.CurrencyEntity


interface ICurrencyLocalDataSourceRepo {
    suspend fun loadExchangeRates(): ExchangeRatesEntity?

    suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity)


//    suspend fun loadCurrencies(): CurrencyEntity?
//
//    suspend fun insertCurrencies(currencies: CurrencyEntity)

}