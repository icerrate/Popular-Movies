package com.icerrate.popularmovies.di

import android.content.Context
import com.icerrate.popularmovies.data.source.MovieDataSource
import com.icerrate.popularmovies.data.source.local.MovieLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt module for database-related dependencies
 * @author Ivan Cerrate
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocalDataSource

    @Provides
    @Singleton
    @LocalDataSource
    fun provideMovieLocalDataSource(@ApplicationContext context: Context): MovieDataSource {
        return MovieLocalDataSource(context)
    }
}