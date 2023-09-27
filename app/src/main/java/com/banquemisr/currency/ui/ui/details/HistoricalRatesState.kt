package com.banquemisr.currency.ui.ui.details

import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse

sealed class HistoricalRatesState {
    data class Loading(var isShow: Boolean) : HistoricalRatesState()
    data class Success(val historicalRates: ArrayList<Any>) : HistoricalRatesState()

    data class ApiError(val date: String) : HistoricalRatesState()
    data object InternetError : HistoricalRatesState()
}