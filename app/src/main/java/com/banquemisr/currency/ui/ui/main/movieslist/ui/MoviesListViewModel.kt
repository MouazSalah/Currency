package com.banquemisr.currency.ui.ui.main.movieslist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.areeb.moviesexplorer.BuildConfig
import com.banquemisr.currency.ui.extesnion.showLogMessage
import com.banquemisr.currency.ui.ui.main.home.ConvertUseCase
import com.banquemisr.currency.ui.ui.main.home.LatestParams
import com.banquemisr.currency.ui.ui.main.home.LatestUseCase
import com.banquemisr.currency.ui.ui.main.home.SymbolsUseCase
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsParams
import com.bumptech.glide.Glide.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val symbolsUseCase: SymbolsUseCase, private val convertUseCase: ConvertUseCase) : ViewModel() {

    private val _moviesListState = MutableStateFlow<MoviesListState>(MoviesListState.Loading)
    val moviesListState: StateFlow<MoviesListState> = _moviesListState

    var convertParams = ConvertParams()

    var sourceCurrency : String ?= null
    var destinationCurrency : String ?= null

    var isSettingTextProgrammatically = false
    var inputSource : InputValueSource = InputValueSource.FROM

    private fun fetchSymbols() {

        viewModelScope.launch {
            _moviesListState.value = MoviesListState.Loading

            val symbolsResponse = symbolsUseCase.invoke(SymbolsParams())
            "symbols success = ${symbolsResponse.success}".showLogMessage()

            val convertResponse = convertUseCase.invoke(
                ConvertParams(
                    currencyFrom = "USD",
                    currencyTo = "EGP",
                    amount = 1.0
                )
            )
            "convert success = ${convertResponse.success}".showLogMessage()

            try {
                _moviesListState.value = MoviesListState.SymbolsSuccess(symbolsResponse)
            } catch (e: Exception) {
                _moviesListState.value = MoviesListState.SymbolsError(e.message ?: "An error occurred")
            }
        }
    }

    fun convertAmount() {

        viewModelScope.launch {
            _moviesListState.value = MoviesListState.Loading

//            val resultAmount = if (inputSource == InputValueSource.FROM) {
//                convertParams.amount?.times(31)
//            } else {
//                convertParams.amount?.div(31)
//            }
//
//            _moviesListState.value = MoviesListState.ConvertSuccess(ConvertResponse(
//                date = null,
//                result = resultAmount,
//                success = null,
//                query = null,
//                info = null
//            ))

            convertParams.date = getCurrentDate()
            convertParams.accessKey = BuildConfig.API_ACCESS_KEY
            val convertResponse = convertUseCase.invoke(convertParams)

            "convert success = ${convertResponse.success}".showLogMessage()

            try {
                _moviesListState.value = MoviesListState.ConvertSuccess(convertResponse)
            } catch (e: Exception) {
                _moviesListState.value = MoviesListState.ConvertError(e.message ?: "An error occurred")
            }
        }
    }

    fun getCurrentDate() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
