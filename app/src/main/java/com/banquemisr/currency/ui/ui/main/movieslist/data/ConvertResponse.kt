package com.banquemisr.currency.ui.ui.main.movieslist.data

import com.google.gson.annotations.SerializedName

data class ConvertResponse(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("result")
	val result: Double? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("query")
	val query: ConvertQuery? = null,

	@field:SerializedName("info")
	val info: ConvertInfo? = null
)

data class ConvertQuery(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("from")
	val from: String? = null,

	@field:SerializedName("to")
	val to: String? = null
)

data class ConvertInfo(

	@field:SerializedName("rate")
	val rate: Any? = null,

	@field:SerializedName("timestamp")
	val timestamp: Int? = null
)
