package com.banquemisr.currency.ui.di

import com.banquemisr.currency.ui.data.CurrencyLocalDataSourceRepoImpl
import com.banquemisr.currency.ui.data.ICurrencyLocalDataSourceRepo
import com.banquemisr.currency.ui.data.IMoviesIRemoteDataSourceRepo
import com.banquemisr.currency.ui.data.MoviesWebServices
import com.banquemisr.currency.ui.data.RemoteDataSourceRepoImpl
import com.banquemisr.currency.ui.db.CurrencyDao
import com.banquemisr.currency.ui.domain.repository.ICurrencyRepository
import com.banquemisr.currency.ui.domain.repository.CurrencyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRemoteDataSourceRepo(apiInterface: MoviesWebServices): IMoviesIRemoteDataSourceRepo = RemoteDataSourceRepoImpl(apiInterface)


    @Singleton
    @Provides
    fun provideLocalDataSource(moviesDao : CurrencyDao): ICurrencyLocalDataSourceRepo = CurrencyLocalDataSourceRepoImpl(moviesDao)


    @Singleton
    @Provides
    fun provideMoviesRepository(remoteDataSource: RemoteDataSourceRepoImpl): ICurrencyRepository = CurrencyRepositoryImpl(remoteDataSource)

}