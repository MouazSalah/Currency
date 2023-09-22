package com.banquemisr.currency.ui.network

import com.areeb.moviesexplorer.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request : Request.Builder =  buildAPIRequest(chain)

        return chain.proceed(request.build())
    }
}

fun buildAPIRequest(chain: Interceptor.Chain): Request.Builder {

    return chain.request().newBuilder()
                           //  .addHeader("apikey", BuildConfig.API_KEY)
}
