package com.banquemisr.currency.ui.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.banquemisr.currency.databinding.FragmentHistoricalRatesBinding
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.extesnion.castToActivity
import com.banquemisr.currency.ui.ui.base.BaseFragment
import com.banquemisr.currency.ui.ui.base.MainActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoricalRatesFragment : BaseFragment<FragmentHistoricalRatesBinding>() {

    private val ratesAdapter by lazy { HistoricalRatesAdapter() }
    private val viewModel : HistoricalRatesViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHistoricalRatesBinding {
        return FragmentHistoricalRatesBinding.inflate(inflater, container, false)
    }

    override fun onFragmentReady() {
        initViews()
        initObservers()
    }


    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.historicalRatesState.collect{ state ->
                when (state) {
                    is HistoricalRatesState.Success -> {
                        ratesAdapter.setList(state.historicalRates)
                        drawBarChart(historyRateResponse = viewModel.historicalRateResponse!!, dates = viewModel.historicalDates, currencies = viewModel.historicalCurrencies)
                    }
                    is HistoricalRatesState.Loading -> { }
                }
            }
        }
    }

    private fun initViews() {

        binding.apply {

            rvHistoricalRates.adapter = ratesAdapter

            castToActivity<MainActivity> {

                it?.mBinding?.apply {
                    clToolbar.isVisible = true
                    tvTitle.text = "Historical Rates"
                    btnBack.isVisible = false
                }
            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
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
}