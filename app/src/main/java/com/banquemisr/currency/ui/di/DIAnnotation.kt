package com.banquemisr.currency.ui.di

import javax.inject.Qualifier

object DIAnnotation {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyInterceptor
}