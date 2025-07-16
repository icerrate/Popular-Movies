package com.icerrate.popularmovies.data.source

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.model.TrailersResponse
import com.icerrate.popularmovies.view.common.BaseCallback

/**
 * @author Ivan Cerrate.
 */
class MovieRepository private constructor(
    private val localDataSource: MovieDataSource,
    private val remoteDataSource: MovieDataSource
) : MovieDataSource {

    companion object {
        @Volatile
        private var INSTANCE: MovieRepository? = null

        fun getInstance(
            localDataSource: MovieDataSource,
            remoteDataSource: MovieDataSource
        ): MovieRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieRepository(localDataSource, remoteDataSource).also { INSTANCE = it }
            }
        }
    }

    override fun getPopularMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        remoteDataSource.getPopularMovies(page, callback)
    }

    override fun getTopRatedMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        remoteDataSource.getTopRatedMovies(page, callback)
    }

    override fun searchMovies(query: String, page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>) {
        remoteDataSource.searchMovies(query, page, callback)
    }

    override fun getMovieTrailers(movieId: Int?, callback: BaseCallback<TrailersResponse<Trailer>>) {
        remoteDataSource.getMovieTrailers(movieId, callback)
    }

    override fun getMovieReviews(movieId: Int?, callback: BaseCallback<PaginatedResponse<Review>>) {
        remoteDataSource.getMovieReviews(movieId, callback)
    }

    override fun getFavoriteMovies(callback: BaseCallback<ArrayList<Movie>>) {
        localDataSource.getFavoriteMovies(callback)
    }

    override fun isFavoriteMovie(movieId: Int, callback: BaseCallback<Boolean>) {
        localDataSource.isFavoriteMovie(movieId, callback)
    }

    override fun addFavoriteMovie(movie: Movie, callback: BaseCallback<Unit>) {
        localDataSource.addFavoriteMovie(movie, callback)
    }

    override fun removeFavoriteMovie(movieId: Int, callback: BaseCallback<Unit>) {
        localDataSource.removeFavoriteMovie(movieId, callback)
    }
}