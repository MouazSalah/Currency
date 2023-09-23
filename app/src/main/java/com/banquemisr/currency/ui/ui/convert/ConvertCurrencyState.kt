package com.banquemisr.currency.ui.ui.convert

import com.banquemisr.currency.ui.data.ConvertResponse
import com.banquemisr.currency.ui.data.SymbolsResponse

sealed class ConvertCurrencyState {
    data object Loading : ConvertCurrencyState()
    data class SymbolsSuccess(val symbolsResponse: SymbolsResponse) : ConvertCurrencyState()
    data class ConvertSuccess(val convertResponse: ConvertResponse) : ConvertCurrencyState()
    data class SymbolsError(val errorMessage: String) : ConvertCurrencyState()
    data class ConvertError(val errorMessage: String) : ConvertCurrencyState()
}
