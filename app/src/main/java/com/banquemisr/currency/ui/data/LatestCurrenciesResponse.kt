package com.banquemisr.currency.ui.data

import com.google.gson.annotations.SerializedName

data class LatestCurrenciesResponse(

    @field:SerializedName("date")
	val date: String? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("rates")
	val rates: Rates? = null,

    @field:SerializedName("timestamp")
	val timestamp: Int? = null,

    @field:SerializedName("base")
	val base: String? = null
)

data class Rates(

	@field:SerializedName("AUD")
	val aUD: Any? = null,

	@field:SerializedName("CHF")
	val cHF: Any? = null,

	@field:SerializedName("JPY")
	val jPY: Any? = null,

	@field:SerializedName("GBP")
	val gBP: Any? = null,

	@field:SerializedName("CAD")
	val cAD: Any? = null,

	@field:SerializedName("USD")
	val uSD: Any? = null,

	@field:SerializedName("CNY")
	val cNY: Any? = null
)
