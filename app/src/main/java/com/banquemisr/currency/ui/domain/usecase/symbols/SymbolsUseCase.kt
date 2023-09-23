package com.banquemisr.currency.ui.domain.usecase.symbols

import com.banquemisr.currency.ui.data.SymbolsParams
import com.banquemisr.currency.ui.data.SymbolsResponse
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import javax.inject.Inject

class SymbolsUseCase @Inject constructor(private val repository: ICurrencyRepository) {

    suspend operator fun invoke(params: SymbolsParams): SymbolsResponse {

        return repository.getSymbols(params)
    }
}
