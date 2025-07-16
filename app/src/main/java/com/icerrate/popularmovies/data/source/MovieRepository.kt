package com.icerrate.popularmovies.data.source

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Resource
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.model.TrailersResponse

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

    override suspend fun getPopularMovies(page: Int?): Resource<PaginatedResponse<Movie>> =
        remoteDataSource.getPopularMovies(page)

    override suspend fun getTopRatedMovies(page: Int?): Resource<PaginatedResponse<Movie>> =
        remoteDataSource.getTopRatedMovies(page)

    override suspend fun searchMovies(
        query: String,
        page: Int?
    ): Resource<PaginatedResponse<Movie>> =
        remoteDataSource.searchMovies(query, page)

    override suspend fun getMovieTrailers(movieId: Int?): Resource<TrailersResponse<Trailer>> =
        remoteDataSource.getMovieTrailers(movieId)

    override suspend fun getMovieReviews(movieId: Int?): Resource<PaginatedResponse<Review>> =
        remoteDataSource.getMovieReviews(movieId)

    override suspend fun getFavoriteMovies(): Resource<ArrayList<Movie>> =
        localDataSource.getFavoriteMovies()

    override suspend fun isFavoriteMovie(movieId: Int): Resource<Boolean> =
        localDataSource.isFavoriteMovie(movieId)

    override suspend fun addFavoriteMovie(movie: Movie): Resource<Unit> =
        localDataSource.addFavoriteMovie(movie)

    override suspend fun removeFavoriteMovie(movieId: Int): Resource<Unit> =
        localDataSource.removeFavoriteMovie(movieId)
}