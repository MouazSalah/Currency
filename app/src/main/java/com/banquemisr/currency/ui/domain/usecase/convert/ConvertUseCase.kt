package com.banquemisr.currency.ui.domain.usecase.convert

import com.banquemisr.currency.ui.data.ConvertParams
import com.banquemisr.currency.ui.data.ConvertResponse
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import javax.inject.Inject

class ConvertUseCase @Inject constructor(private val repository: ICurrencyRepository) {

    suspend operator fun invoke(params: ConvertParams): ConvertResponse {

        return repository.convertAmount(params)
    }

}
