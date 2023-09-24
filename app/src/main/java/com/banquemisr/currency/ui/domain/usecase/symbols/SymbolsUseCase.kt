package com.banquemisr.currency.ui.domain.usecase.symbols

import com.banquemisr.currency.ui.data.model.symbols.SymbolsParams
import com.banquemisr.currency.ui.data.model.symbols.SymbolsResponse
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import javax.inject.Inject

class SymbolsUseCase @Inject constructor(private val repository: ICurrencyRepository) {

    suspend operator fun invoke(params: SymbolsParams): List<String> {

        return repository.getSymbols(params)
    }
}
