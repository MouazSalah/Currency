package com.banquemisr.currency.ui.data.model.rates

data class ExchangeRatesApiModel(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)