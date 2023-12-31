package com.banquemisr.currency.ui.di

import android.content.Context
import com.banquemisr.currency.ui.data.room.DataStoreManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()


    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context, gson: Gson) = DataStoreManager(context, gson)
}