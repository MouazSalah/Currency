package com.banquemisr.currency.ui.ui.main.movieslist.data

import com.banquemisr.currency.ui.core.HashMapParams
import com.google.gson.annotations.SerializedName

data class MoviesListParams(
    @SerializedName("api_key")
    var apiKey: String? = null,

    @SerializedName("page")
    var page: Int? = null
) : HashMapParams {
    override fun dataClass(): Any = this
}