package com.banquemisr.currency.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ExchangeRates(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

@Entity(tableName = "exchange_rates")
data class ExchangeRatesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
