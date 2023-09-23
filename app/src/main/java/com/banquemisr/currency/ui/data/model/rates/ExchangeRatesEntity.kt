package com.banquemisr.currency.ui.data.model.rates

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRatesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
