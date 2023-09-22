package com.banquemisr.currency.ui.di

import com.banquemisr.currency.ui.network.IMoviesIRemoteDataSourceRepo
import com.banquemisr.currency.ui.network.MoviesWebServices
import com.banquemisr.currency.ui.network.RemoteDataSourceRepoImpl
import com.banquemisr.currency.ui.ui.main.movieslist.domain.IMovieRepository
import com.banquemisr.currency.ui.ui.main.movieslist.domain.MovieRepositoryImpl
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
    fun provideMoviesRepository(remoteDataSource: RemoteDataSourceRepoImpl): IMovieRepository = MovieRepositoryImpl(remoteDataSource)

}