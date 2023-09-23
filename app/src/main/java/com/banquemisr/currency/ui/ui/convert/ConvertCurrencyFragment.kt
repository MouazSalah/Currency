package com.banquemisr.currency.ui.ui.convert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.banquemisr.currency.R
import com.banquemisr.currency.databinding.FragmentConvertCurrencyBinding
import com.banquemisr.currency.ui.extesnion.afterTextChanged
import com.banquemisr.currency.ui.extesnion.castToActivity
import com.banquemisr.currency.ui.extesnion.formatAmount
import com.banquemisr.currency.ui.extesnion.getMostCommonCurrencies
import com.banquemisr.currency.ui.extesnion.showLogMessage
import com.banquemisr.currency.ui.ui.base.BaseFragment
import com.banquemisr.currency.ui.ui.base.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConvertCurrencyFragment : BaseFragment<FragmentConvertCurrencyBinding>()
{
    private val viewModel : ConvertCurrencyViewModel by viewModels()
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentConvertCurrencyBinding {
        return FragmentConvertCurrencyBinding.inflate(inflater, container, false)
    }

    override fun onFragmentReady() {

        initViews()
        initObservers()
        initCurrenciesViews()
        initAmountViews()
    }

    private fun initCurrenciesViews() {

        binding.apply {

            val fromCurrenciesList : ArrayList<String> = getMostCommonCurrencies()
            fromCurrenciesList.add(0, "From")

            val toCurrenciesList : ArrayList<String> = getMostCommonCurrencies()
            toCurrenciesList.add(0, "To")

            spinnerYearsFrom.setItems(fromCurrenciesList)
            spinnerYearsTo.setItems(toCurrenciesList)

            spinnerYearsFrom.apply {

                setOnSpinnerOutsideTouchListener { _, _ ->
                    removeSpinnersFocusWhenClickingOutside()
                }

                setOnSpinnerItemSelectedListener<String> { _, _, _, item ->
                    when {
                        selectedIndex != 0 -> {
                            hint = item
                            viewModel.convertParams.currencyFrom = item
                            viewModel.sourceCurrency = item
                            checkIfBothCurrenciesAndAmountAreSelected()
                        }
                        else -> {
                            text = null
                            hint = "From"

                            "before reset from sourceCurrency : ${viewModel.sourceCurrency}".showLogMessage()
                            "before reset from destinationCurrency : ${viewModel.destinationCurrency}".showLogMessage()

                            viewModel.convertParams.currencyFrom = null
                            viewModel.sourceCurrency = null

                            viewModel.isSettingTextProgrammatically = true
                            etPriceTo.text = null
                            viewModel.isSettingTextProgrammatically = false

                            "after reset from sourceCurrency : ${viewModel.sourceCurrency}".showLogMessage()
                            "after reset from destinationCurrency : ${viewModel.destinationCurrency}".showLogMessage()
                        }
                    }
                }
            }

            spinnerYearsTo.apply {

                setOnSpinnerOutsideTouchListener { _, _ ->
                    removeSpinnersFocusWhenClickingOutside()
                }

                setOnSpinnerItemSelectedListener<String> { _, _, _, item ->
                    when {
                        selectedIndex != 0 -> {
                            hint = item
                            viewModel.convertParams.currencyTo = item
                            viewModel.destinationCurrency = item
                            checkIfBothCurrenciesAndAmountAreSelected()
                        }
                        else -> {

                            "before reset to sourceCurrency : ${viewModel.sourceCurrency}".showLogMessage()
                            "before reset to destinationCurrency : ${viewModel.destinationCurrency}".showLogMessage()

                            text = null
                            hint = "To"
                            viewModel.convertParams.currencyTo = null
                            viewModel.destinationCurrency = null

                            viewModel.isSettingTextProgrammatically = true
                            binding.etPriceTo.text = null
                            viewModel.isSettingTextProgrammatically = false

                            "after reset to sourceCurrency : ${viewModel.sourceCurrency}".showLogMessage()
                            "after reset to destinationCurrency : ${viewModel.destinationCurrency}".showLogMessage()
                        }
                    }
                }
            }
        }
    }

    private fun checkIfBothCurrenciesAndAmountAreSelected() {

        "sourceCurrency : ${viewModel.sourceCurrency}".showLogMessage()
        "destinationCurrency : ${viewModel.destinationCurrency}".showLogMessage()
        "amount : ${viewModel.convertParams.amount}".showLogMessage()

        binding.apply {
            if (viewModel.sourceCurrency != null && viewModel.destinationCurrency != null && viewModel.convertParams.amount != null) {
                viewModel.convertAmount()
            }
        }
    }


    private fun initAmountViews() {
        binding.apply {

            etPriceFrom.hint = "From"
            etPriceTo.hint = "To"

            etPriceFrom.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    etPriceFrom.isCursorVisible = true
                    etPriceFrom.setBackgroundResource(R.drawable.et_currency_filled_bg)
                } else {
                    etPriceFrom.isCursorVisible = false
                    etPriceFrom.setBackgroundResource(R.drawable.et_currency_bg)
                }
            }

            etPriceTo.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    etPriceTo.isCursorVisible = true
                    etPriceTo.setBackgroundResource(R.drawable.et_currency_filled_bg)
                } else {
                    etPriceTo.isCursorVisible = false
                    etPriceTo.setBackgroundResource(R.drawable.et_currency_bg)
                }
            }

            etPriceFrom.afterTextChanged {

                "etPriceFrom afterTextChanged".showLogMessage()

                if (!viewModel.isSettingTextProgrammatically) {
                    if (it.isBlank() || it.isEmpty()) {
                        viewModel.convertParams.amount = null
                    } else {
                        viewModel.convertParams.amount = it.replace(",", "").toDoubleOrNull() ?: 0.0

                        viewModel.inputSource = InputValueSource.FROM

                        if (viewModel.sourceCurrency != null && viewModel.destinationCurrency != null) {
                            viewModel.convertParams.currencyFrom = viewModel.sourceCurrency
                            viewModel.convertParams.currencyTo = viewModel.destinationCurrency

                            viewModel.convertAmount()
                        }
                    }
                }
            }

            etPriceTo.afterTextChanged {

                "etPriceTo afterTextChanged".showLogMessage()

                if (!viewModel.isSettingTextProgrammatically) {
                    if (it.isBlank() || it.isEmpty()) {
                        viewModel.convertParams.amount = null
                    } else {
                        viewModel.convertParams.amount = it.replace(",", "").toDoubleOrNull() ?: 0.0

                        viewModel.inputSource = InputValueSource.TO

                        if (viewModel.sourceCurrency != null && viewModel.destinationCurrency != null) {
                            viewModel.sourceCurrency = viewModel.destinationCurrency.also {
                                viewModel.destinationCurrency = viewModel.sourceCurrency
                            }
                            viewModel.convertAmount()
                        }
                    }
                }
            }
        }
    }

    private fun initObservers() {

        lifecycleScope.launch {

            viewModel.convertCurrencyState.collect{ state ->
                when (state) {

                    is ConvertCurrencyState.Loading -> {
                        castToActivity<MainActivity> {
                            it?.showProgress(state.isShow)
                        }
                    }

                    is ConvertCurrencyState.LatestFetchDate -> {
                        binding.layoutLatestDate.isVisible = true
                        "Last Update:  ${state.date}".also { binding.textLatestDate.text = it }
                    }

                    is ConvertCurrencyState.SymbolsSuccess -> {

                        val symbols = state.symbolsResponse.symbols

                        val currenciesList = arrayListOf("EGP" , "USD" , "GHD", "FGD", "EUR")
                    }

                    is ConvertCurrencyState.SymbolsError -> {
                        val errorMessage = state.errorMessage
                        "symbols error message : ${errorMessage.toString()} ".showLogMessage()
                    }

                    is ConvertCurrencyState.ConvertError -> {
                        val errorMessage = state.errorMessage
                        "convert error message : ${errorMessage.toString()} ".showLogMessage()
                    }

                    is ConvertCurrencyState.ConvertSuccess -> {
                        "convert response observe".showLogMessage()
                        viewModel.isSettingTextProgrammatically = true
                        when (viewModel.inputSource) {
                            InputValueSource.FROM -> {
                                binding.etPriceTo.setText(formatAmount(state.convertResponse.result ?: 0.0))
                            }
                            else -> {
                                viewModel.sourceCurrency = viewModel.destinationCurrency.also {
                                    viewModel.destinationCurrency = viewModel.sourceCurrency
                                }
                                binding.etPriceFrom.setText(formatAmount(state.convertResponse.result ?: 0.0))
                            }
                        }
                        viewModel.isSettingTextProgrammatically = false
                    }

                    is ConvertCurrencyState.ApiError -> {

                        Toast.makeText(requireContext(), "api error", Toast.LENGTH_SHORT).show()
                    }
                    ConvertCurrencyState.InternetError -> {
                        Toast.makeText(requireContext(), "internet connection !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initViews() {

        binding.apply {

            refreshIcon.setOnClickListener {
                viewModel.fetchLatestRates()
            }

            icSwitch.setOnClickListener {

                etPriceFrom.text = etPriceTo.text.also {
                    etPriceTo.text = etPriceFrom.text
                }

                val tempIndex = spinnerYearsFrom.selectedIndex
                spinnerYearsFrom.selectItemByIndex(spinnerYearsTo.selectedIndex)
                spinnerYearsTo.selectItemByIndex(tempIndex)

                viewModel.convertAmount()
            }

            layoutParent.setOnTouchListener { view, _ ->
                hideKeyboard()
                removeInputFieldsFocusWhenClickingOutside()
                view.performClick()
                true
            }

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

    private fun removeSpinnersFocusWhenClickingOutside(){
        binding.apply {
            spinnerYearsFrom.dismiss()
            spinnerYearsTo.dismiss()
        }
    }

    private fun removeInputFieldsFocusWhenClickingOutside(){
        binding.apply {
            etPriceFrom.clearFocus()
            etPriceTo.clearFocus()
        }
    }
}
