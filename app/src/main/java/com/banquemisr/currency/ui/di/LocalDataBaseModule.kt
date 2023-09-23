package com.banquemisr.currency.ui.di

import android.content.Context
import androidx.room.Room
import com.banquemisr.currency.ui.data.CurrencyLocalDataSourceRepoImpl
import com.banquemisr.currency.ui.data.ICurrencyLocalDataSourceRepo
import com.banquemisr.currency.ui.db.CurrencyDao
import com.banquemisr.currency.ui.db.RoomAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataBaseModule {

    companion object {
        private const val DATA_BASE_NAME = "IMDB_Movies_database"
    }

    @Singleton
    @Provides
    fun provideIntroDao(dataBase: RoomAppDatabase): CurrencyDao = dataBase.currencyDao()


    @Singleton
    @Provides
    fun provideLocalDataSource(moviesDao : CurrencyDao): ICurrencyLocalDataSourceRepo = CurrencyLocalDataSourceRepoImpl(moviesDao)


    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): RoomAppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RoomAppDatabase::class.java,
            DATA_BASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }
}