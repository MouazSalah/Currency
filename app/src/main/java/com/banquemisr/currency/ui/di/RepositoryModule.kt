package com.banquemisr.currency.ui.di

import com.banquemisr.currency.ui.data.datasource.CurrencyLocalDataSourceRepoImpl
import com.banquemisr.currency.ui.data.datasource.ICurrencyLocalDataSourceRepo
import com.banquemisr.currency.ui.data.api.ICurrencyRemoteDataSourceRepo
import com.banquemisr.currency.ui.data.api.CurrencyWebServices
import com.banquemisr.currency.ui.data.datasource.CurrencyRemoteDataSourceRepoImpl
import com.banquemisr.currency.ui.data.room.CurrencyDao
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
    fun provideRemoteDataSourceRepo(apiInterface: CurrencyWebServices): ICurrencyRemoteDataSourceRepo = CurrencyRemoteDataSourceRepoImpl(apiInterface)


    @Singleton
    @Provides
    fun provideLocalDataSource(currencyDao: CurrencyDao): ICurrencyLocalDataSourceRepo = CurrencyLocalDataSourceRepoImpl(currencyDao)


    @Singleton
    @Provides
    fun provideCurrencyRepository(localDataSource: CurrencyLocalDataSourceRepoImpl, remoteDataSource: CurrencyRemoteDataSourceRepoImpl) :
            ICurrencyRepository = CurrencyRepositoryImpl(localDataSource, remoteDataSource)
}