package com.banquemisr.currency.ui.di

import android.content.Context
import com.banquemisr.currency.ui.network.MovieInterceptor
import com.banquemisr.currency.ui.network.MoviesWebServices
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @DIAnnotation.MoviesRetrofit
    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @DIAnnotation.MoviesInterceptor moviesInterceptor: MovieInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(moviesInterceptor)
        if (com.areeb.moviesexplorer.BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .collector(ChuckerCollector(context))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
        }
        return okHttpClientBuilder.build()
    }

    @DIAnnotation.MoviesRetrofit
    @Provides
    fun provideAuthRetrofit(@DIAnnotation.MoviesRetrofit okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(com.areeb.moviesexplorer.BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    @Singleton
    @Provides
    fun provideAuthApiService(@DIAnnotation.MoviesRetrofit retrofit: Retrofit): MoviesWebServices = retrofit.create(
        MoviesWebServices::class.java)


    @DIAnnotation.MoviesInterceptor
    @Singleton
    @Provides
    fun provideMoviesInterceptor(): MovieInterceptor = MovieInterceptor()
}