package com.banquemisr.currency.ui.ui.convert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.banquemisr.currency.R
import com.banquemisr.currency.databinding.FragmentConvertCurrencyBinding
import com.banquemisr.currency.ui.extesnion.afterTextChanged
import com.banquemisr.currency.ui.extesnion.castToActivity
import com.banquemisr.currency.ui.extesnion.formatPrice
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
            fromCurrenciesList.add(0, "من")

            val toCurrenciesList : ArrayList<String> = getMostCommonCurrencies()
            toCurrenciesList.add(0, "الي")

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
                            hint = "من"

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
                            hint = "الي"
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

            etPriceFrom.hint = "من"
            etPriceTo.hint = "الي"

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
                            viewModel.convertParams.currencyFrom = viewModel.destinationCurrency
                            viewModel.convertParams.currencyTo = viewModel.sourceCurrency
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
                    ConvertCurrencyState.Loading -> {
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
                                binding.etPriceTo.setText(formatPrice(state.convertResponse.result ?: 0.0))
                            }
                            else -> {
                                binding.etPriceFrom.setText(formatPrice(state.convertResponse.result ?: 0.0))
                            }
                        }
                        viewModel.isSettingTextProgrammatically = false
                    }
                }
            }
        }
    }

    private fun initViews() {

        binding.apply {

            // I want to switch the spinner to value to be in the spinner from value and vice versa
            // and also I want to switch the values of edittext
            icSwitch.setOnClickListener {

                    val tempCurrencyFrom = viewModel.convertParams.currencyFrom
                    val tempCurrencyTo = viewModel.convertParams.currencyTo

                    val tempSourceCurrency = viewModel.sourceCurrency
                    val tempDestinationCurrency = viewModel.destinationCurrency

                    val tempAmount = viewModel.convertParams.amount

                    viewModel.convertParams.currencyFrom = tempCurrencyTo
                    viewModel.convertParams.currencyTo = tempCurrencyFrom

                    viewModel.sourceCurrency = tempDestinationCurrency
                    viewModel.destinationCurrency = tempSourceCurrency

                    viewModel.convertParams.amount = tempAmount

                    viewModel.isSettingTextProgrammatically = true

                    etPriceFrom.setText(etPriceTo.text.toString())
                    etPriceTo.setText(etPriceFrom.text.toString())

                    viewModel.isSettingTextProgrammatically = false

                    spinnerYearsFrom.text = tempCurrencyTo
                    spinnerYearsTo.text = tempCurrencyFrom
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