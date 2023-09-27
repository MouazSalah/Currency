package com.banquemisr.currency.ui.ui.convert

sealed class ConvertCurrencyState {
    data class Loading(var isShow: Boolean) : ConvertCurrencyState()
    data class SymbolsSuccess(val symbols: List<String>) : ConvertCurrencyState()
    data class ConvertSuccess(val result: Double) : ConvertCurrencyState()

    data class LatestFetchDate(val date: String, val timeAgo: String) : ConvertCurrencyState()

    data class ApiError(val date: String) : ConvertCurrencyState()
    data object InternetError : ConvertCurrencyState()
}
