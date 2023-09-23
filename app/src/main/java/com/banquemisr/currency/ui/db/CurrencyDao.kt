package com.banquemisr.currency.ui.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.banquemisr.currency.ui.data.ExchangeRatesEntity

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeRates: ExchangeRatesEntity)

    @Query("SELECT * FROM exchange_rates")
    suspend fun getExchangeRates(): ExchangeRatesEntity?
}