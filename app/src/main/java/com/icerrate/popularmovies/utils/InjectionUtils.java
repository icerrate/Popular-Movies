package com.icerrate.popularmovies.utils;

import android.content.Context;

import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.data.source.local.MovieLocalDataSource;
import com.icerrate.popularmovies.data.source.remote.MovieRemoteDataSource;
import com.icerrate.popularmovies.provider.cloud.RetrofitModule;

/**
 * @author Ivan Cerrate.
 */

public class InjectionUtils {

    public static MovieRepository movieRepository(Context context) {
        return MovieRepository.getInstance(
                new MovieLocalDataSource(context),
                new MovieRemoteDataSource(RetrofitModule.get().provideMovieAPI())
        );
    }
}
