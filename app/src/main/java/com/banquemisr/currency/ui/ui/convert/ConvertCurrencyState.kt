package com.banquemisr.currency.ui.ui.convert

import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse

sealed class ConvertCurrencyState {
    data class Loading(var isShow: Boolean) : ConvertCurrencyState()
    data class SymbolsSuccess(val symbolsResponse: SymbolsResponse) : ConvertCurrencyState()
    data class ConvertSuccess(val convertResponse: ConvertResponse) : ConvertCurrencyState()
    data class SymbolsError(val errorMessage: String) : ConvertCurrencyState()
    data class ConvertError(val errorMessage: String) : ConvertCurrencyState()

    data class LatestFetchDate(val date: String) : ConvertCurrencyState()
}
