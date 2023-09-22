package com.banquemisr.currency.ui.ui.main.home

import com.banquemisr.currency.ui.core.HashMapParams
import com.google.gson.annotations.SerializedName

data class LatestParams(
    @SerializedName("access_key")
    var accessKey: String? = null,

    @SerializedName("base")
    var base: String? = null,

    @SerializedName("symbols")
    var symbols: String? = null,

) : HashMapParams {
    override fun dataClass(): Any = this
}