package com.banquemisr.currency.ui.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity)

    @Query("SELECT * FROM exchange_rates")
    suspend fun getExchangeRates(): ExchangeRatesEntity?


//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertCurrencies(currencyEntity: CurrencyEntity)
//
//    @Query("SELECT * FROM cashed_currencies")
//    suspend fun getCurrencies(): CurrencyEntity?
}