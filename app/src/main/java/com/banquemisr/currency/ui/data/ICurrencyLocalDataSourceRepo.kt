package com.banquemisr.currency.ui.data


interface ICurrencyLocalDataSourceRepo {
    suspend fun loadExchangeRates(): ExchangeRatesEntity?

    suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity)

}