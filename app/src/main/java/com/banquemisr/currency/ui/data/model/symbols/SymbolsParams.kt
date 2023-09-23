package com.banquemisr.currency.ui.data.model.symbols

import com.banquemisr.currency.ui.core.HashMapParams
import com.google.gson.annotations.SerializedName

data class SymbolsParams(
    @SerializedName("access_key")
    var accessKey: String? = null,

    @SerializedName("format")
    var format: Int = 1
) : HashMapParams {
    override fun dataClass(): Any = this
}