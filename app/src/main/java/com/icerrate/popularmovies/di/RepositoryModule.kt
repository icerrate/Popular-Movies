package com.icerrate.popularmovies.di

import com.icerrate.popularmovies.data.source.MovieDataSource
import com.icerrate.popularmovies.data.source.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository dependencies
 * @author Ivan Cerrate
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        @DatabaseModule.LocalDataSource localDataSource: MovieDataSource,
        @NetworkModule.RemoteDataSource remoteDataSource: MovieDataSource
    ): MovieRepository {
        return MovieRepository.getInstance(localDataSource, remoteDataSource)
    }
}