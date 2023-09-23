package com.banquemisr.currency.ui.data

import com.banquemisr.currency.ui.data.ExchangeRatesEntity

interface ICurrencyLocalDataSourceRepo {
    suspend fun loadExchangeRates(): ExchangeRatesEntity?

    suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity)

}