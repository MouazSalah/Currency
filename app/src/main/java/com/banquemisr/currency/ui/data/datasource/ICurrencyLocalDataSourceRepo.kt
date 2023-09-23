package com.banquemisr.currency.ui.data.datasource

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity


interface ICurrencyLocalDataSourceRepo {
    suspend fun loadExchangeRates(): ExchangeRatesEntity?

    suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity)

}