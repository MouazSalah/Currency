package com.banquemisr.currency.ui.domain.mapper

import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesApiModel
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesEntity
import com.banquemisr.currency.ui.data.model.rates.ExchangeRatesUIModel

object ExchangeRatesMapper {

    fun mapApiModelToRoomModel(apiModel: ExchangeRatesApiModel): ExchangeRatesEntity {
        return ExchangeRatesEntity(
            base = apiModel.base,
            date = apiModel.date,
            rates = apiModel.rates
        )
    }


    fun mapRoomModelToUIModel(roomModel: ExchangeRatesEntity): ExchangeRatesUIModel {
        return ExchangeRatesUIModel(
            base = roomModel.base,
            date = roomModel.date,
            rates = roomModel.rates
        )
    }

    fun mapApiModelToUIModel(apiModel: ExchangeRatesApiModel): ExchangeRatesUIModel {
        return ExchangeRatesUIModel(
            base = apiModel.base,
            date = apiModel.date,
            rates = apiModel.rates
        )
    }
}