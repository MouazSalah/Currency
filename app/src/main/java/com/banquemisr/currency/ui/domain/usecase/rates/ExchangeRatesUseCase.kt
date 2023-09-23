package com.banquemisr.currency.ui.domain.usecase.rates

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesParams
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import com.banquemisr.currency.ui.network.ApiResult
import javax.inject.Inject

class ExchangeRatesUseCase @Inject constructor(private val repository: ICurrencyRepository) {

    suspend operator fun invoke(params: ExchangeRatesParams): ApiResult<ExchangeRatesUIModel> {
        return repository.getExchangeRatesFromApi(params)
    }
}

