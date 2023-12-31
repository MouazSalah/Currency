package com.banquemisr.currency.ui.data.model.rates

import com.banquemisr.currency.ui.core.HashMapParams
import com.google.gson.annotations.SerializedName

data class ExchangeRatesParams(
    @SerializedName("access_key")
    var accessKey: String? = null,

    @SerializedName("base")
    var base: String? = null,

    @SerializedName("symbols")
    var symbols: String? = null,

) : HashMapParams {
    override fun dataClass(): Any = this
}