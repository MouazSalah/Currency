package com.banquemisr.currency.ui.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity

@Database(entities = [ExchangeRatesEntity::class], version = 1, exportSchema = false)
@TypeConverters(MapTypeConverter::class)
abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
