package com.banquemisr.currency.ui.data.datasource

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity
import com.banquemisr.currency.ui.db.CurrencyDao
import com.banquemisr.currency.ui.db.CurrencyEntity
import javax.inject.Inject

class CurrencyLocalDataSourceRepoImpl @Inject constructor(private val currencyDao: CurrencyDao) :
    ICurrencyLocalDataSourceRepo {

    override suspend fun loadExchangeRates(): ExchangeRatesEntity? = currencyDao.getExchangeRates()

    override suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity) = currencyDao.insertExchangeRates(exchangeRates)
//    override suspend fun loadCurrencies(): CurrencyEntity?  = currencyDao.getCurrencies()
//
//    override suspend fun insertCurrencies(currencies: CurrencyEntity) = currencyDao.insertCurrencies(currencies)

}