package com.banquemisr.currency.ui.ui.main.home

import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.SymbolsResponse
import com.banquemisr.currency.ui.ui.main.movieslist.domain.IMovieRepository
import javax.inject.Inject

class SymbolsUseCase @Inject constructor(private val repository: IMovieRepository) {

    suspend operator fun invoke(params: SymbolsParams): SymbolsResponse {

        return repository.getSymbols(params)
    }

}
