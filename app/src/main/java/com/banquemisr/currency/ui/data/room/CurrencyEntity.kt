package com.banquemisr.currency.ui.data.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cashed_currencies", indices = [Index(value = ["currencyName"], unique = true)])
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // You can add an ID if needed
    val strings: String // Store the list of strings as a single string
)