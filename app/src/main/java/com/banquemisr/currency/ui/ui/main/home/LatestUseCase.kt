package com.banquemisr.currency.ui.ui.main.home

import com.banquemisr.currency.ui.ui.main.movieslist.domain.IMovieRepository
import javax.inject.Inject

class LatestUseCase @Inject constructor(private val repository: IMovieRepository) {

    suspend operator fun invoke(params: LatestParams): LatestCurrenciesResponse {

        return repository.getLatest(params)
    }

}
