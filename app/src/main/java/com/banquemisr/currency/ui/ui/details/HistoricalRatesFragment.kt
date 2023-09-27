package com.banquemisr.currency.ui.ui.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.banquemisr.currency.R
import com.banquemisr.currency.databinding.FragmentHistoricalRatesBinding
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.extesnion.castToActivity
import com.banquemisr.currency.ui.ui.base.BaseFragment
import com.banquemisr.currency.ui.ui.base.MainActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class HistoricalRatesFragment : BaseFragment<FragmentHistoricalRatesBinding>() {

    private val moviesAdapter by lazy { HistoricalRatesAdapter() }
    private val viewModel : HistoricalRatesViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHistoricalRatesBinding {
        return FragmentHistoricalRatesBinding.inflate(inflater, container, false)
    }

    override fun onFragmentReady() {

        initViews()
        initObservers()
    }

    private fun generateBarData(dates: List<String>, exchangeRates: List<Double>): BarData {
        val barEntries = ArrayList<BarEntry>()

        for (i in dates.indices) {
            val date = dates[i]
            val rate = exchangeRates[i].toFloat()
            barEntries.add(BarEntry(i.toFloat(), rate, date))
        }

        val dataSet = BarDataSet(barEntries, "Exchange Rates")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        return BarData(dataSet)
    }


    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.historicalRatesState.collect{ state ->
                when (state) {
                    is HistoricalRatesState.ApiError -> {

                    }
                    HistoricalRatesState.InternetError -> {

                    }
                    is HistoricalRatesState.Loading -> {

                    }
                    is HistoricalRatesState.Success -> {
                        moviesAdapter.setList(state.historicalRates)

                        drawLineChart(historyRateResponse = viewModel.historicalRateResponse!!, dates = viewModel.historicalDates, currencies = viewModel.historicalCurrencies)
                        drawBarChart(historyRateResponse = viewModel.historicalRateResponse!!, dates = viewModel.historicalDates, currencies = viewModel.historicalCurrencies)
                        drawBubbleChart(binding.bubbleCharts, historyRateResponse = viewModel.historicalRateResponse!!, dates = viewModel.historicalDates, currencies = viewModel.historicalCurrencies)
                        drawCandleStickChart(binding.candlesCharts, historyRateResponse = viewModel.historicalRateResponse!!, dates = viewModel.historicalDates)
                        drawCombinedChart(binding.combinedCharts, historyRateResponse = viewModel.historicalRateResponse!!, dates = viewModel.historicalDates, currencies = viewModel.historicalCurrencies)
                    }
                }
            }
        }
    }

    private fun initViews() {

        binding.apply {

            rvHistoricalRates.adapter = moviesAdapter

            castToActivity<MainActivity> {

                it?.mBinding?.apply {
                    clToolbar.isVisible = true
                    tvTitle.text = "Movies"
                    btnBack.isVisible = false
                }
            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finishAffinity()
        }
        callback.isEnabled = true
    }


    private fun drawBarChart(historyRateResponse: HistoryRateResponse, dates: List<String>, currencies: List<String>) {

        val barEntries = mutableListOf<BarEntry>()

        for (i in dates.indices) {
            val date = dates[i]
            val currencyEntries = mutableListOf<Float>()

            for (currency in currencies) {
                val rate = historyRateResponse.rates[currency]?.toFloat() ?: 0f // Default value for missing currencies
                currencyEntries.add(rate)
            }

            barEntries.add(BarEntry(i.toFloat(), currencyEntries.toFloatArray()))
        }

        val barDataSet = BarDataSet(barEntries, "")
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        barDataSet.stackLabels = currencies.toTypedArray()

        val barData = BarData(barDataSet)
        binding.barCharts.data = barData

        val xAxis = binding.barCharts.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        binding.barCharts.description.text = "Exchange Rates for the Last Three Days"
        binding.barCharts.setFitBars(true)
        binding.barCharts.invalidate()
    }

    private fun drawLineChart(
        historyRateResponse: HistoryRateResponse,
        dates: List<String>,
        currencies: List<String>
    ) {
        val lineEntries = mutableListOf<List<Entry>>()

        for (currency in currencies) {
            val currencyEntries = mutableListOf<Entry>()

            for (i in dates.indices) {
                val date = dates[i]
                val rate = historyRateResponse.rates[currency]?.toFloat() ?: 0f // Default value for missing currencies
                currencyEntries.add(Entry(i.toFloat(), rate))
            }

            lineEntries.add(currencyEntries)
        }

        val lineDataSets = mutableListOf<LineDataSet>()

        for (i in currencies.indices) {
            val lineDataSet = LineDataSet(lineEntries[i], currencies[i])
            lineDataSet.color = ColorTemplate.COLORFUL_COLORS[i]
            lineDataSet.setCircleColor(ColorTemplate.COLORFUL_COLORS[i])
            lineDataSet.setDrawValues(false)
            lineDataSets.add(lineDataSet)
        }

        val lineData = LineData(lineDataSets as List<ILineDataSet>?)
        binding.lineChart.data = lineData

        val xAxis = binding.lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        binding.lineChart.description.text = "Exchange Rates for the Last Three Days"
        binding.lineChart.invalidate()
    }


    fun drawBubbleChart(
        bubbleChart: BubbleChart,
        historyRateResponse: HistoryRateResponse,
        dates: List<String>,
        currencies: List<String>
    ) {
        val bubbleEntries = mutableListOf<List<BubbleEntry>>()

        for (currency in currencies) {
            val currencyEntries = mutableListOf<BubbleEntry>()

            for (i in dates.indices) {
                val date = dates[i]
                val rate = historyRateResponse.rates[currency]?.toFloat() ?: 0f // Default value for missing currencies
                currencyEntries.add(BubbleEntry(i.toFloat(), rate, rate))
            }

            bubbleEntries.add(currencyEntries)
        }

        val bubbleDataSets = mutableListOf<BubbleDataSet>()

        for (i in currencies.indices) {
            val bubbleDataSet = BubbleDataSet(bubbleEntries[i], currencies[i])
            bubbleDataSet.color = ColorTemplate.COLORFUL_COLORS[i]
            bubbleDataSet.setDrawValues(false)
            bubbleDataSets.add(bubbleDataSet)
        }

        val bubbleData = BubbleData(bubbleDataSets as List<IBubbleDataSet>?)
        bubbleChart.data = bubbleData

        val xAxis = bubbleChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        bubbleChart.description.text = "Exchange Rates for the Last Three Days"
        bubbleChart.invalidate()
    }


    fun drawCandleStickChart(
        candleStickChart: CandleStickChart,
        historyRateResponse: HistoryRateResponse,
        dates: List<String>
    ) {
        val candleEntries = mutableListOf<List<CandleEntry>>()

        // Extract open, close, high, and low values from the historyRateResponse for each date
        for (date in dates) {
            val open = historyRateResponse.rates[date]?.toFloat() ?: 0f
            val close = open // For simplicity, assuming open and close are the same
            val high = open // For simplicity, assuming high is the same as open
            val low = open // For simplicity, assuming low is the same as open

            val candleEntry = CandleEntry(
                dates.indexOf(date).toFloat(),
                high,
                low,
                open,
                close
            )
            candleEntries.add(listOf(candleEntry))
        }

        val candleDataSets = mutableListOf<CandleDataSet>()

        val candleDataSet = CandleDataSet(candleEntries[0], "Exchange Rates")
        candleDataSet.color = ColorTemplate.COLORFUL_COLORS[0]
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSets.add(candleDataSet)

        val candleData = CandleData(candleDataSets as List<ICandleDataSet>?)
        candleStickChart.data = candleData

        val xAxis = candleStickChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        candleStickChart.description.text = "Candlestick Chart for Exchange Rates"
        candleStickChart.invalidate()
    }


    fun drawCombinedChart(
        combinedChart: CombinedChart,
        historyRateResponse: HistoryRateResponse,
        dates: List<String>,
        currencies: List<String>
    ) {
        val combinedData = CombinedData()

        // Create LineData for line chart
        val lineEntries = mutableListOf<List<Entry>>()

        for (currency in currencies) {
            val currencyEntries = mutableListOf<Entry>()

            for (i in dates.indices) {
                val date = dates[i]
                val rate = historyRateResponse.rates[currency]?.toFloat() ?: 0f // Default value for missing currencies
                currencyEntries.add(Entry(i.toFloat(), rate))
            }

            lineEntries.add(currencyEntries)
        }

        val lineDataSets = mutableListOf<ILineDataSet>()

        for (i in currencies.indices) {
            val lineDataSet = LineDataSet(lineEntries[i], currencies[i])
            lineDataSet.color = ColorTemplate.COLORFUL_COLORS[i]
            lineDataSet.setCircleColor(ColorTemplate.COLORFUL_COLORS[i])
            lineDataSet.setDrawValues(false)
            lineDataSets.add(lineDataSet)
        }

        val lineData = LineData(lineDataSets)
        combinedData.setData(lineData)

        // Create BarData for bar chart
        val barEntries = mutableListOf<List<BarEntry>>()

        for (currency in currencies) {
            val currencyEntries = mutableListOf<BarEntry>()

            for (i in dates.indices) {
                val date = dates[i]
                val rate = historyRateResponse.rates[currency]?.toFloat() ?: 0f // Default value for missing currencies
                currencyEntries.add(BarEntry(i.toFloat(), rate))
            }

            barEntries.add(currencyEntries)
        }

        val barDataSets = mutableListOf<IBarDataSet>()

        for (i in currencies.indices) {
            val barDataSet = BarDataSet(barEntries[i], currencies[i])
            barDataSet.color = ColorTemplate.COLORFUL_COLORS[i]
            barDataSets.add(barDataSet)
        }

        val barData = BarData(barDataSets)
        combinedData.setData(barData)

        // Create CandleData for candlestick chart
        val candleEntries = mutableListOf<List<CandleEntry>>()

        for (date in dates) {
            val open = historyRateResponse.rates[date]?.toFloat() ?: 0f
            val close = open
            val high = open
            val low = open

            val candleEntry = CandleEntry(
                dates.indexOf(date).toFloat(),
                high,
                low,
                open,
                close
            )
            candleEntries.add(listOf(candleEntry))
        }

        val candleDataSets = mutableListOf<ICandleDataSet>()

        val candleDataSet = CandleDataSet(candleEntries[0], "Candlestick")
        candleDataSet.color = ColorTemplate.COLORFUL_COLORS[currencies.size]
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSets.add(candleDataSet)

        val candleData = CandleData(candleDataSets)
        combinedData.setData(candleData)

        // Set combined chart data
        combinedChart.data = combinedData

        // Configure x-axis
        val xAxis = combinedChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        // Set chart description
        combinedChart.description.text = "Combined Chart for Exchange Rates"
        combinedChart.invalidate()
    }





}