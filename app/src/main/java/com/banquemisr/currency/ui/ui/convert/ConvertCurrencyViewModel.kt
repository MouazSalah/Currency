package com.banquemisr.currency.ui.ui.convert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banquemisr.currency.BuildConfig
import com.banquemisr.currency.ui.extesnion.showLogMessage
import com.banquemisr.currency.ui.data.ExchangeRates
import com.banquemisr.currency.ui.domain.usecase.convert.ConvertUseCase
import com.banquemisr.currency.ui.data.LatestParams
import com.banquemisr.currency.ui.domain.usecase.rates.ExchangeRatesUseCase
import com.banquemisr.currency.ui.domain.usecase.symbols.SymbolsUseCase
import com.banquemisr.currency.ui.data.ConvertParams
import com.banquemisr.currency.ui.data.ConvertResponse
import com.banquemisr.currency.ui.data.SymbolsParams
import com.banquemisr.currency.ui.db.DataStoreManager
import com.banquemisr.currency.ui.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.cache2.Relay.Companion.edit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(
    private val exchangeRatesUseCase: ExchangeRatesUseCase,
    private val symbolsUseCase: SymbolsUseCase,
    private val dataStoreManager: DataStoreManager,
    private val convertUseCase: ConvertUseCase) : ViewModel()
{
    private val _convertCurrencyState = MutableStateFlow<ConvertCurrencyState>(ConvertCurrencyState.Loading)
    val convertCurrencyState: StateFlow<ConvertCurrencyState> = _convertCurrencyState

    var convertParams = ConvertParams()

    var amount: Double ?= null
    var sourceCurrency : String ?= null
    var destinationCurrency : String ?= null

    var isSettingTextProgrammatically = false
    var inputSource : InputValueSource = InputValueSource.FROM

    var exchangeRates: ExchangeRates? = null

    init {
        getLastFetchDate()
        fetchLatestRates()
    }

    private fun fetchLatestRates() {

        viewModelScope.launch {

            when (val result = exchangeRatesUseCase(LatestParams(accessKey = BuildConfig.API_ACCESS_KEY, base = null, symbols = null))) {
                is ApiResult.Success -> {
                    exchangeRates = result.data
                }
                is ApiResult.Error -> {
                    val exception = result.exception
                    // Handle error
                }
            }
        }
    }

    suspend fun saveLastFetchDate() {
        dataStoreManager.setLastFetchDate(getCurrentDate(), viewModelScope)
    }

    private fun getLastFetchDate() {
        val fetchedDate = dataStoreManager.getLastFetchDate()
        "${fetchedDate.toString()}".showLogMessage()

        if (fetchedDate.isNullOrEmpty()) {
            "fetched date is empty".showLogMessage()
        } else {
            "fetched date = ${fetchedDate}".showLogMessage()

        }
    }

    private fun fetchSymbols() {

        viewModelScope.launch {
            _convertCurrencyState.value = ConvertCurrencyState.Loading

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
                _convertCurrencyState.value = ConvertCurrencyState.SymbolsSuccess(symbolsResponse)
            } catch (e: Exception) {
                _convertCurrencyState.value =
                    ConvertCurrencyState.SymbolsError(e.message ?: "An error occurred")
            }
        }
    }

    fun convertAmount() {

        viewModelScope.launch {

            _convertCurrencyState.value = ConvertCurrencyState.Loading

            val amount =  if (exchangeRates != null) {
                val fromRate = exchangeRates!!.rates[sourceCurrency]
                val toRate = exchangeRates!!.rates[destinationCurrency]
                val baseAmount = amount?.div(fromRate!!)
                baseAmount?.times(toRate!!)
            } else {
                0.0 // Handle case where exchange rates are not available
            }

            _convertCurrencyState.value = ConvertCurrencyState.ConvertSuccess(
                ConvertResponse(
                    date = null,
                    result = amount,
                    success = null,
                    query = null,
                    info = null
                )
            )

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

//            convertParams.date = getCurrentDate()
//            convertParams.accessKey = BuildConfig.API_ACCESS_KEY
//            val convertResponse = convertUseCase.invoke(convertParams)
//
//            "convert success = ${convertResponse.success}".showLogMessage()
//
//            try {
//                _moviesListState.value = MoviesListState.ConvertSuccess(convertResponse)
//            } catch (e: Exception) {
//                _moviesListState.value = MoviesListState.ConvertError(e.message ?: "An error occurred")
//            }
        }
    }

    fun getCurrentDate() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
