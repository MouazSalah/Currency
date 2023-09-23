package com.banquemisr.currency.ui.data

import com.banquemisr.currency.ui.core.HashMapParams
import com.google.gson.annotations.SerializedName

data class ConvertParams(
    @SerializedName("access_key")
    var accessKey: String? = null,

    @SerializedName("to")
    var currencyTo: String? = null,

    @SerializedName("from")
    var currencyFrom: String ?= null,

    @SerializedName("date")
    var date: String ?= null,

    @SerializedName("amount")
    var amount: Double ?= null

) : HashMapParams {
    override fun dataClass(): Any = this
}