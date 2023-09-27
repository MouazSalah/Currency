package com.banquemisr.currency.ui.domain.usecase.history

import com.banquemisr.currency.ui.db.CurrencyEntity
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import com.banquemisr.currency.ui.network.ApiResult
import javax.inject.Inject

class CashedCurrenciesUseCase @Inject constructor(private val repository: ICurrencyRepository) {
//    suspend fun getCurrencies(): ApiResult<CurrencyEntity> {
//        return repository.getCurrencies()
//    }
//
//    suspend fun cashCurrencies(currencyEntity: CurrencyEntity) {
//        return repository.insertCurrencies(currencyEntity)
//    }
}