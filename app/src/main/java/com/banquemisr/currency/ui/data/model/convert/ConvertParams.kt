package com.banquemisr.currency.ui.data.model.convert

data class ConvertParams(
    var sourceCurrency: String? = null,
    var destinationCurrency: String? = null,
    var amount: Double? = null
)