package com.banquemisr.currency.ui.network

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class CashedData<out T>(val data: T) : ApiResult<T>()
    data class ApiError(val exception: String) : ApiResult<Nothing>()

    data class InternetError(val exception: String) : ApiResult<Nothing>()
}