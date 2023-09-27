package com.banquemisr.currency.ui.ui.details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.banquemisr.currency.databinding.FragmentHistoricalRatesBinding
import com.banquemisr.currency.ui.extesnion.castToActivity
import com.banquemisr.currency.ui.ui.base.BaseFragment
import com.banquemisr.currency.ui.ui.base.MainActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

                        val dates = listOf("2023-09-23", "2023-09-24", "2023-09-25")
                        val exchangeRates = listOf(1.0, 1.1, 1.2)

                        val barData = generateBarData(dates, exchangeRates)

                        binding.barCharts.data = barData
                        binding.barCharts.description.text = "Exchange Rates Chart"
                        binding.barCharts.xAxis.valueFormatter = IndexAxisValueFormatter(dates) // Set x-axis labels
                        binding.barCharts.animateXY(2000, 2000)
                        binding.barCharts.invalidate()


//                        val barData = getDataSet()
//                        binding.barCharts.data = barData
//                        binding.barCharts.description.text = "Exchange Rates Chart"
//                        binding.barCharts.animateXY(2000, 2000)
//                        binding.barCharts.invalidate()

//                        // Customize chart appearance and behavior
//                        binding.lineChart.description = Description().apply {
//                            text = "Currency Exchange Rates"
//                            textSize = 12f
//                        }
//                        binding.lineChart.setDrawGridBackground(false)
//
//                        // Create a list of data points (entries)
//                        val entries = ArrayList<Entry>()
//                        entries.add(Entry(1f, 30f))
//                        entries.add(Entry(2f, 32f))
//                        entries.add(Entry(3f, 28f))
//                        // Add more entries as needed
//
//                        // Create a LineDataSet to hold the data and customize its appearance
//                        val dataSet = LineDataSet(entries, "Exchange Rate")
//                        dataSet.setDrawCircles(true)
//                        dataSet.color = Color.BLUE
//                        dataSet.lineWidth = 2f
//
//                        // Create a list of LineDataSets (if you have multiple lines on the chart)
//                        val dataSets = ArrayList<ILineDataSet>()
//                        dataSets.add(dataSet)
//
//                        // Create a LineData object from the datasets
//                        val lineData = LineData(dataSets)
//
//                        // Set the data for the chart
//                        binding.lineChart.data = lineData
//
//                        // Refresh the chart
//                        binding.lineChart.invalidate()


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


    private fun getDataSet(): BarData {

        val valueSet1 = ArrayList<BarEntry>()
        valueSet1.add(BarEntry(0f, 110.000f)) // Jan
        valueSet1.add(BarEntry(1f, 40.000f))  // Feb
        valueSet1.add(BarEntry(2f, 60.000f))  // Mar
        valueSet1.add(BarEntry(3f, 30.000f))  // Apr
        valueSet1.add(BarEntry(4f, 90.000f))  // May
        valueSet1.add(BarEntry(5f, 100.000f)) // Jun


        val valueSet2 = ArrayList<BarEntry>()
        valueSet2.add(BarEntry(0f, 110.000f)) // Jan
        valueSet2.add(BarEntry(1f, 40.000f))  // Feb
        valueSet2.add(BarEntry(2f, 60.000f))  // Mar
        valueSet2.add(BarEntry(3f, 30.000f))  // Apr
        valueSet2.add(BarEntry(4f, 90.000f))  // May
        valueSet2.add(BarEntry(5f, 100.000f)) // Jun

        val barDataSet1 = BarDataSet(valueSet1, "Brand 1")
        barDataSet1.color = Color.rgb(0, 155, 0)

        val barDataSet2 = BarDataSet(valueSet2, "Brand 2")
        barDataSet2.colors = ColorTemplate.COLORFUL_COLORS.toList()

        val barData = BarData(barDataSet1, barDataSet2)

        return barData
    }

    private fun getXAxisValues(): ArrayList<String> {
        val xAxis = ArrayList<String>()
        xAxis.add("yesterday")
        xAxis.add("2-23-09-25")
        xAxis.add("2-23-09-24")
        return xAxis
    }
}