package com.banquemisr.currency.ui.data

import com.banquemisr.currency.ui.db.CurrencyDao
import javax.inject.Inject

class CurrencyLocalDataSourceRepoImpl @Inject constructor(private val moviesDao: CurrencyDao) :
    ICurrencyLocalDataSourceRepo {

    override suspend fun loadExchangeRates(): ExchangeRatesEntity? = moviesDao.getExchangeRates()

    override suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity) = moviesDao.insertExchangeRates(exchangeRates)

}