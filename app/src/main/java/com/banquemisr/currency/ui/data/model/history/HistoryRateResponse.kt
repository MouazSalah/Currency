package com.banquemisr.currency.ui.data.model.history

import com.google.gson.annotations.SerializedName

data class HistoryRateResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("timestamp")
    val timestamp: Int,

    @SerializedName("historical")
    val historical: Boolean,

    @SerializedName("base")
    val base: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("rates")
    val rates: Map<String, Double>
)
