package com.banquemisr.currency.ui.ui.convert

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.banquemisr.currency.R
import com.banquemisr.currency.databinding.FragmentConvertCurrencyBinding
import com.banquemisr.currency.ui.extesnion.afterTextChanged
import com.banquemisr.currency.ui.extesnion.castToActivity
import com.banquemisr.currency.ui.extesnion.formatAmount
import com.banquemisr.currency.ui.ui.apierror.BottomSheetServerError
import com.banquemisr.currency.ui.ui.base.BaseFragment
import com.banquemisr.currency.ui.ui.base.MainActivity
import com.banquemisr.currency.ui.ui.nointernet.NoInternetActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConvertCurrencyFragment : BaseFragment<FragmentConvertCurrencyBinding>()
{
    private val viewModel : ConvertCurrencyViewModel by viewModels()
    var countDownTimer: CountDownTimer? = null
    val totalTimeInMilliseconds = 60000 // 10 seconds

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentConvertCurrencyBinding {
        return FragmentConvertCurrencyBinding.inflate(inflater, container, false)
    }

    private val noInternetForResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.fetchLatestRates()
            viewModel.startRepeatingTask()
        }
    }

    override fun onFragmentReady() {

        initViews()
        initObservers()
        initCurrenciesViews()
        initAmountViews()
        initCountDownTimer()
    }

    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(totalTimeInMilliseconds.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = 100 - ((millisUntilFinished * 100) / totalTimeInMilliseconds)
                binding.circularCountdownView.setProgress(progress.toFloat())
                binding.circularCountdownView.timeLeft = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                binding.circularCountdownView.setProgress(100f)
                binding.circularCountdownView.timeLeft = 0

                binding.circularCountdownView.isVisible = false
                binding.refreshIcon.isVisible = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.etPriceFrom.text = null
        binding.etPriceTo.text = null
        viewModel.apply {
            fetchAllSymbols()
            fetchLatestRates()
            startRepeatingTask()
        }
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }


    private fun initCurrenciesViews() {

        binding.apply {

            spinnerYearsFrom.apply {

                setOnSpinnerOutsideTouchListener { _, _ ->
                    removeSpinnersFocusWhenClickingOutside()
                }

                setOnSpinnerItemSelectedListener<String> { _, _, _, item ->
                    when {
                        selectedIndex != 0 -> {
                            hint = item
                            viewModel.convertParams.sourceCurrency = item
                            checkIfBothCurrenciesAndAmountAreSelected()
                        }
                        else -> {
                            text = null
                            hint = "From"

                            viewModel.convertParams.sourceCurrency = null

                            viewModel.isSettingTextProgrammatically = true
                            etPriceTo.text = null
                            viewModel.isSettingTextProgrammatically = false
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
                            viewModel.convertParams.destinationCurrency = item
                            checkIfBothCurrenciesAndAmountAreSelected()
                        }
                        else -> {
                            text = null
                            hint = "To"
                            viewModel.convertParams.destinationCurrency = null

                            viewModel.isSettingTextProgrammatically = true
                            binding.etPriceTo.text = null
                            viewModel.isSettingTextProgrammatically = false
                        }
                    }
                }
            }
        }
    }

    private fun checkIfBothCurrenciesAndAmountAreSelected() {

        binding.apply {
            if (viewModel.convertParams.sourceCurrency != null && viewModel.convertParams.destinationCurrency != null && viewModel.convertParams.amount != null) {
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

                if (!viewModel.isSettingTextProgrammatically) {
                    if (it.isBlank() || it.isEmpty()) {
                        viewModel.convertParams.amount = null
                    } else {
                        viewModel.convertParams.amount = it.replace(",", "").toDoubleOrNull() ?: 0.0

                        if (viewModel.convertParams.sourceCurrency != null && viewModel.convertParams.destinationCurrency != null) {
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
                        binding.refreshIcon.isVisible = false
                        binding.circularCountdownView.isVisible = true
                        countDownTimer?.cancel()
                        countDownTimer?.start()

                        binding.layoutLatestDate.isVisible = true
                        "Last Update:  ${state.date}".also { binding.textLatestDate.text = it }
                        state.timeAgo.also { binding.tvTimeAgo.text = it }
                    }
                    is ConvertCurrencyState.SymbolsSuccess -> {

                        val fromCurrenciesList : ArrayList<String> = ArrayList()
                        fromCurrenciesList.add(0, "From")
                        fromCurrenciesList.addAll(state.symbols)
                        binding.spinnerYearsFrom.setItems(fromCurrenciesList)

                        val toCurrenciesList : ArrayList<String> = ArrayList()
                        toCurrenciesList.add(0, "To")
                        toCurrenciesList.addAll(state.symbols)
                        binding.spinnerYearsTo.setItems(toCurrenciesList)
                    }
                    is ConvertCurrencyState.ConvertSuccess -> {
                        viewModel.isSettingTextProgrammatically = true
                        binding.etPriceTo.setText(formatAmount(state.convertResponse.result ?: 0.0))
                        viewModel.isSettingTextProgrammatically = false
                    }
                    is ConvertCurrencyState.ApiError -> {
                        BottomSheetServerError().show(requireActivity().supportFragmentManager, null)
                    }
                    ConvertCurrencyState.InternetError -> {
                        val intent = Intent(requireActivity(), NoInternetActivity::class.java)
                        noInternetForResultActivity.launch(intent)
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

            btnOk.setOnClickListener {
                findNavController().navigate(R.id.action_converterFragment_to_historicalRatesFragment)
            }

            icSwitch.setOnClickListener {

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
                    tvTitle.text = "Currency"
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
