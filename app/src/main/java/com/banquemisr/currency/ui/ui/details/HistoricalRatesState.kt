package com.banquemisr.currency.ui.ui.details


sealed class HistoricalRatesState {
    data class Loading(var isShow: Boolean) : HistoricalRatesState()
    data class Success(val historicalRates: ArrayList<Any>) : HistoricalRatesState()
}