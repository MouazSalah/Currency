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
import com.banquemisr.currency.ui.db.DataStoreManager
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
    private val dataStoreManager: DataStoreManager) : ViewModel()
{
    private val _convertCurrencyState = MutableStateFlow<ConvertCurrencyState>(ConvertCurrencyState.Loading(false))
    val convertCurrencyState: StateFlow<ConvertCurrencyState> = _convertCurrencyState

    private var refreshLatestDateJob: Job? = null

    var convertParams = ConvertParams()

    var amount: Double ?= null
    var sourceCurrency : String ?= null
    var destinationCurrency : String ?= null

    var isSettingTextProgrammatically = false
    var inputSource : InputValueSource = InputValueSource.FROM

    var exchangeRates: ExchangeRatesUIModel? = null

    init {
        fetchLatestRates()
        startRepeatingTask()
    }

    fun fetchLatestRates() {

        viewModelScope.launch {
            when (val result = exchangeRatesUseCase(ExchangeRatesParams(accessKey = BuildConfig.API_ACCESS_KEY, base = null, symbols = null))) {
                is ApiResult.Success -> {
                    "ApiResult.Success".showLogMessage()
                    exchangeRates = result.data
                    saveLastFetchDate()
                    _convertCurrencyState.value = ConvertCurrencyState.LatestFetchDate(getLastFetchedDate(getCurrentTimeInMilliSeconds()))
                }
                is ApiResult.ApiError -> {
                    "ApiResult.ApiError".showLogMessage()
                    val exception = result.exception
                    getLastFetchDate()

                }
                is ApiResult.InternetError -> {
                    "ApiResult.InternetError".showLogMessage()
                    val exception = result.exception
                    getLastFetchDate()
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
        }
    }

    private fun getLastFetchedDate(lastFetchedDate : Long) : String {
        return "${lastFetchedDate.toFormattedDate()} - ${TimeAgo.getTimeAgo(lastFetchedDate)}"
    }

    private fun startRepeatingTask() {

        refreshLatestDateJob = viewModelScope.launch {
                while (true) {
                    getLastFetchDate()
                    delay( 60 * 1000L) // Delay for 60 seconds
                }
        }
    }

    override fun onCleared() {
        refreshLatestDateJob?.cancel()
        super.onCleared()
    }
}