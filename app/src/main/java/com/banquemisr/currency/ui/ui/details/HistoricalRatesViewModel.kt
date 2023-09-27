package com.banquemisr.currency.ui.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banquemisr.currency.BuildConfig
import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.db.DataStoreManager
import com.banquemisr.currency.ui.domain.usecase.history.HistoricalRatesUseCase
import com.banquemisr.currency.ui.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HistoricalRatesViewModel @Inject constructor(
    private val historicalRatesUseCase: HistoricalRatesUseCase,
    private val dataStoreManager: DataStoreManager) : ViewModel()
{
    private val _historicalRatesState = MutableStateFlow<HistoricalRatesState>(HistoricalRatesState.Loading(false))
    val historicalRatesState: StateFlow<HistoricalRatesState> = _historicalRatesState

    var historicalRates = ArrayList<Any>()
    var historicalCurrencies = ArrayList<String>()
    var historicalDates = ArrayList<String>()

    var historicalRateResponse: HistoryRateResponse? = null

    init {
        getHistoricalDates()
        fetchHistoricalRates()
    }


    private fun fetchHistoricalRates() {
        viewModelScope.launch {

            val yesterdayRatesDeferred = fetchHistoricalRatesAsync(historicalDates[0])
            val dayBeforeYesterdayRatesDeferred = fetchHistoricalRatesAsync(historicalDates[1])
            val dayBeforeBeforeYesterdayRatesDeferred = fetchHistoricalRatesAsync(historicalDates[2])

            val yesterdayRates = yesterdayRatesDeferred
            val dayBeforeYesterdayRates = dayBeforeYesterdayRatesDeferred
            val dayBeforeBeforeYesterdayRates = dayBeforeBeforeYesterdayRatesDeferred

            val desiredCurrencyCodes = getCurrentList()

            if (yesterdayRates is ApiResult.Success) {
                historicalRateResponse = yesterdayRates.data
                val currencyRates = extractCurrencyRates(yesterdayRates.data, desiredCurrencyCodes)
                historicalRates.add(historicalDates[0])
                historicalRates.addAll(currencyRates)
            }

            if (dayBeforeYesterdayRates is ApiResult.Success) {
                historicalRateResponse = dayBeforeYesterdayRates.data
                val currencyRates = extractCurrencyRates(dayBeforeYesterdayRates.data, desiredCurrencyCodes)
                historicalRates.add(historicalDates[1])
                historicalRates.addAll(currencyRates)
            }

            if (dayBeforeBeforeYesterdayRates is ApiResult.Success) {
                historicalRateResponse = dayBeforeBeforeYesterdayRates.data
                val currencyRates = extractCurrencyRates(dayBeforeBeforeYesterdayRates.data, desiredCurrencyCodes)
                historicalRates.add(historicalDates[2])
                historicalRates.addAll(currencyRates)
            }

            _historicalRatesState.value = HistoricalRatesState.Success(historicalRates)
        }
    }

    private suspend fun fetchHistoricalRatesAsync(date: String): ApiResult<HistoryRateResponse> {
        return CoroutineScope(Dispatchers.IO).async {
            historicalRatesUseCase(
                HistoricalRatesParams(
                    accessKey = BuildConfig.API_ACCESS_KEY,
                    date = date
                )
            )
        }.await()
    }

    private fun getHistoricalDates(){
        val today = LocalDate.now()

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val yesterday = today.minusDays(1)
        val yesterdayFormatted = yesterday.format(dateFormatter)

        val dayBeforeYesterday = today.minusDays(2)
        val dayBeforeYesterdayFormatted = dayBeforeYesterday.format(dateFormatter)

        val dayBeforeBeforeYesterday = today.minusDays(2)
        val dayBeforeBeforeYesterdayFormatted = dayBeforeBeforeYesterday.format(dateFormatter)

        historicalDates.clear()
        historicalDates.add(yesterdayFormatted)
        historicalDates.add(dayBeforeYesterdayFormatted)
        historicalDates.add(dayBeforeBeforeYesterdayFormatted)
    }


    private fun extractCurrencyRates(response: HistoryRateResponse, desiredCurrencyCodes: ArrayList<String>): List<HistoricalRate> {
        val currencyRates = mutableListOf<HistoricalRate>()

        for (currencyCode in desiredCurrencyCodes) {
            val rate = response.rates[currencyCode]
            if (rate != null) {
                val currencyName = response.rates.entries
                    .find { it.key == currencyCode }
                    ?.key

                if (currencyName != null) {
                    currencyRates.add(HistoricalRate(currencyName, rate))
                }
            }
        }

        return currencyRates
    }

    private fun getCurrentList(): ArrayList<String> {

        historicalCurrencies.clear()
        historicalCurrencies.addAll(dataStoreManager.getCurrenciesList())

        return historicalCurrencies
    }
}