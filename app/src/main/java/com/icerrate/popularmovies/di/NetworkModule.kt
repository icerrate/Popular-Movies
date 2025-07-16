package com.icerrate.popularmovies.di

import com.icerrate.popularmovies.data.source.MovieDataSource
import com.icerrate.popularmovies.data.source.remote.MovieRemoteDataSource
import com.icerrate.popularmovies.provider.cloud.RetrofitModule
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt module for network-related dependencies
 * @author Ivan Cerrate
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RemoteDataSource

    @Provides
    @Singleton
    fun provideMovieAPI(): MovieAPI {
        return RetrofitModule.get().provideMovieAPI()
    }

    @Provides
    @Singleton
    @RemoteDataSource
    fun provideMovieRemoteDataSource(movieAPI: MovieAPI): MovieDataSource {
        return MovieRemoteDataSource(movieAPI)
    }
}