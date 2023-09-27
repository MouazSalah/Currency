package com.banquemisr.currency.ui.domain.usecase.history

import com.banquemisr.currency.ui.data.model.history.HistoricalRatesParams
import com.banquemisr.currency.ui.data.model.history.HistoryRateResponse
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import com.banquemisr.currency.ui.extesnion.showLogMessage
import com.banquemisr.currency.ui.network.ApiResult
import javax.inject.Inject

class HistoricalRatesUseCase @Inject constructor(private val repository: ICurrencyRepository) {
    suspend operator fun invoke(params: HistoricalRatesParams): ApiResult<HistoryRateResponse> {
        return repository.getHistoricalRates(params)
    }
}

