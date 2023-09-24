package com.banquemisr.currency.ui.ui.convert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banquemisr.currency.BuildConfig
import com.banquemisr.currency.ui.core.TimeAgo
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.domain.usecase.rates.ExchangeRatesUseCase
import com.banquemisr.currency.ui.data.model.convert.ConvertParams
import com.banquemisr.currency.ui.data.model.convert.ConvertResponse
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.db.DataStoreManager
import com.banquemisr.currency.ui.domain.usecase.symbols.SymbolsUseCase
import com.banquemisr.currency.ui.extesnion.getCurrentTimeInMilliSeconds
import com.banquemisr.currency.ui.extesnion.showLogMessage
import com.banquemisr.currency.ui.extesnion.toFormattedDate
import com.banquemisr.currency.ui.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor(
    private val exchangeRatesUseCase: ExchangeRatesUseCase,
    private val symbolsUseCase: SymbolsUseCase,
    private val dataStoreManager: DataStoreManager) : ViewModel()
{
    private val _convertCurrencyState = MutableStateFlow<ConvertCurrencyState>(ConvertCurrencyState.Loading(false))
    val convertCurrencyState: StateFlow<ConvertCurrencyState> = _convertCurrencyState

    private var refreshLatestDateJob: Job? = null

    var convertParams = ConvertParams()

    var sourceCurrency : String ?= null
    var destinationCurrency : String ?= null

    var isSettingTextProgrammatically = false
    var inputSource : InputValueSource = InputValueSource.FROM
    private var exchangeRates: ExchangeRatesUIModel? = null

    var symbolsList = ArrayList<String>()

    init {
        fetchLatestRates()
        startRepeatingTask()
    }

    private fun fetchAllSymbols() {

        viewModelScope.launch {
            val result = symbolsUseCase(SymbolsParams(accessKey = BuildConfig.API_ACCESS_KEY, format = 1))
            symbolsList.addAll(result)
            "symbols size = ${result.size}".showLogMessage()

            _convertCurrencyState.value = ConvertCurrencyState.SymbolsSuccess(symbolsList)
        }
    }

    fun fetchLatestRates() {

        viewModelScope.launch {

            val result = exchangeRatesUseCase(ExchangeRatesParams(accessKey = BuildConfig.API_ACCESS_KEY, base = null, symbols = null))
            when (result) {
                is ApiResult.Success -> {
                    exchangeRates = result.data
                    saveLastFetchDate()
                    _convertCurrencyState.value = ConvertCurrencyState.LatestFetchDate(getLastFetchedDate(getCurrentTimeInMilliSeconds()))
                }
                is ApiResult.ApiError -> {
                    val exception = result.exception
                    "viewModel api error = ${exception.toString()}".showLogMessage()
                    _convertCurrencyState.value = ConvertCurrencyState.ApiError( "")
                }
                is ApiResult.InternetError -> {
                    val exception = result.exception
                    "viewModel internet error = ${exception.toString()}".showLogMessage()
                    _convertCurrencyState.value = ConvertCurrencyState.InternetError
                }
                is ApiResult.CashedData -> {
                    exchangeRates = result.data
                }
            }
        }
    }

    private fun saveLastFetchDate() {
        dataStoreManager.setLastFetchDate(getCurrentTimeInMilliSeconds(), viewModelScope)
    }

    private fun getLastFetchDate() {
        viewModelScope.launch {
            val fetchedDate : Long = dataStoreManager.getLastFetchDate() ?: getCurrentTimeInMilliSeconds()
            if (fetchedDate == 0.toLong()) {
                _convertCurrencyState.value = ConvertCurrencyState.LatestFetchDate(getLastFetchedDate(
                    getCurrentTimeInMilliSeconds()
                ))
            }
            else {
                _convertCurrencyState.value = ConvertCurrencyState.LatestFetchDate(getLastFetchedDate(fetchedDate ?: getCurrentTimeInMilliSeconds()))
            }
        }
    }

    fun convertAmount() {

        viewModelScope.launch {

            exchangeRates?.let { rates ->
                val fromRate = rates.rates[sourceCurrency]
                val toRate = rates.rates[destinationCurrency]

                "sourceCurrency : ${fromRate}".showLogMessage()
                "destinationCurrency : ${toRate}".showLogMessage()
                "amount : ${convertParams.amount}".showLogMessage()

                fromRate?.let { fromRateValue ->
                    toRate?.let { toRateValue ->
                        val baseAmount = convertParams.amount?.div(fromRateValue)
                        val convertedAmount = baseAmount?.times(toRateValue)

                        _convertCurrencyState.value = ConvertCurrencyState.ConvertSuccess(
                            ConvertResponse(
                                date = null,
                                result = convertedAmount,
                                success = null,
                                query = null,
                                info = null
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getLastFetchedDate(lastFetchedDate : Long) : String {
        return "${lastFetchedDate.toFormattedDate()} - ${TimeAgo.getTimeAgo(lastFetchedDate)}"
    }

    fun startRepeatingTask() {

        refreshLatestDateJob = viewModelScope.launch {
                while (true) {
                    getLastFetchDate()
                    delay( 60 * 1000L)
                }
        }
    }

    override fun onCleared() {
        refreshLatestDateJob?.cancel()
        super.onCleared()
    }
}