package com.banquemisr.currency.ui.domain.usecase.rates

import com.banquemisr.currency.ui.data.LatestParams
import com.banquemisr.currency.ui.data.ExchangeRates
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import com.banquemisr.currency.ui.network.ApiResult
import javax.inject.Inject

class ExchangeRatesUseCase @Inject constructor(private val repository: ICurrencyRepository) {

    suspend operator fun invoke(params: LatestParams): ApiResult<ExchangeRates> {
        return repository.getExchangeRates(params)
    }
}

