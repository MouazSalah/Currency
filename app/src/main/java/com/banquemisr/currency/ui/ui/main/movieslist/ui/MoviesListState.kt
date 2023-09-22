package com.banquemisr.currency.ui.ui.main.movieslist.ui

import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsResponse

sealed class MoviesListState {
    data object Loading : MoviesListState()
    data class SymbolsSuccess(val symbolsResponse: SymbolsResponse) : MoviesListState()
    data class ConvertSuccess(val convertResponse: ConvertResponse) : MoviesListState()
    data class SymbolsError(val errorMessage: String) : MoviesListState()
    data class ConvertError(val errorMessage: String) : MoviesListState()
}
