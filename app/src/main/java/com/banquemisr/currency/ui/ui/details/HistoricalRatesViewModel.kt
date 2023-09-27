package com.banquemisr.currency.ui.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banquemisr.currency.BuildConfig
import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.db.DataStoreManager
import com.banquemisr.currency.ui.domain.usecase.history.HistoricalRatesUseCase
import com.banquemisr.currency.ui.extesnion.showLogMessage
import com.banquemisr.currency.ui.network.ApiResult
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        getDaysDates()
        fetchHistoricalRates()
    }

    fun getDaysDates(){
        // Get today's date
        val today = LocalDate.now()

        // Format the date in "yyyy-MM-dd" format
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val todayFormatted = today.format(dateFormatter)

        // Get yesterday's date by subtracting one day
        val yesterday = today.minusDays(1)
        val yesterdayFormatted = yesterday.format(dateFormatter)

        // Get the day before yesterday's date by subtracting two days
        val dayBeforeYesterday = today.minusDays(2)
        val dayBeforeYesterdayFormatted = dayBeforeYesterday.format(dateFormatter)

        val dayBeforeBeforeYesterday = today.minusDays(2)
        val dayBeforeBeforeYesterdayFormatted = dayBeforeBeforeYesterday.format(dateFormatter)

        // Print the dates
        println("Today: $todayFormatted")
        println("Yesterday: $yesterdayFormatted")
        println("Day Before Yesterday: $dayBeforeYesterdayFormatted")
        println("Day Before Before Yesterday: $dayBeforeBeforeYesterdayFormatted")
    }

    private fun fetchHistoricalRates() {

        viewModelScope.launch {

            val result = historicalRatesUseCase(
                HistoricalRatesParams(
                    accessKey = BuildConfig.API_ACCESS_KEY,
                    date = "2023-09-23"
                )
            )

            if (result is ApiResult.Success) {

                val desiredCurrencyCodes = listOf("USD", "AED", "EGP", "EUR")
                val currencyRates = extractCurrencyRates(result.data, desiredCurrencyCodes)

                val date1 = "2023-09-23"
                historicalRates.add(date1)
                historicalRates.addAll(currencyRates)

//                var date2 = "2023-09-24"
//                historicalRates.add(date2)
//                historicalRates.addAll(currencyRates)
//
//                var date3 = "2023-09-25"
//                historicalRates.add(date3)
//                historicalRates.addAll(currencyRates)

                _historicalRatesState.value = HistoricalRatesState.Success(historicalRates)

                "historical_rates = ${currencyRates.toString()}".showLogMessage()
            }
        }
    }


    // Function to extract currency names and rates
    private fun extractCurrencyRates(response: HistoryRateResponse, desiredCurrencyCodes: List<String>): List<HistoricalRate> {
        val currencyRates = mutableListOf<HistoricalRate>()

        for (currencyCode in desiredCurrencyCodes) {
            val rate = response.rates[currencyCode]
            if (rate != null) {
                val currencyName = response.rates.entries
                    .find { it.key == currencyCode }
                    ?.key // Assuming the currency code is also the currency name

                if (currencyName != null) {
                    currencyRates.add(HistoricalRate(currencyName, rate))
                }
            }
        }

        return currencyRates
    }
}