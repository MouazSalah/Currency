package com.banquemisr.currency.ui.network

data class ErrorResponse(
    val success: Boolean,
    val error: ErrorDetail
)

data class ErrorDetail(
    val code: Int,
    val type: String,
    val info: String
)