package com.banquemisr.currency.ui.ui.main.home

import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertParams
import com.banquemisr.currency.ui.ui.main.movieslist.data.ConvertResponse
import com.banquemisr.currency.ui.ui.main.movieslist.domain.IMovieRepository
import javax.inject.Inject

class ConvertUseCase @Inject constructor(private val repository: IMovieRepository) {

    suspend operator fun invoke(params: ConvertParams): ConvertResponse {

        return repository.convertAmount(params)
    }

}
