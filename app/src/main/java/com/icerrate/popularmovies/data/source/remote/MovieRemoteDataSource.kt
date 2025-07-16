package com.icerrate.popularmovies.data.source.remote

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Resource
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.model.TrailersResponse
import com.icerrate.popularmovies.data.source.MovieDataSource
import com.icerrate.popularmovies.extensions.safeApiCall
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author Ivan Cerrate.
 */
class MovieRemoteDataSource(
    private val movieAPI: MovieAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieDataSource {

    override suspend fun getPopularMovies(page: Int?): Resource<PaginatedResponse<Movie>> =
        safeApiCall(dispatcher) {
            movieAPI.getPopularMovies(page)
        }

    override suspend fun getTopRatedMovies(page: Int?): Resource<PaginatedResponse<Movie>> =
        safeApiCall(dispatcher) {
            movieAPI.getTopRatedMovies(page)
        }

    override suspend fun searchMovies(query: String, page: Int?): Resource<PaginatedResponse<Movie>> =
        safeApiCall(dispatcher) {
            movieAPI.searchMovies(query, page)
        }

    override suspend fun getMovieTrailers(movieId: Int?): Resource<TrailersResponse<Trailer>> =
        safeApiCall(dispatcher) {
            movieAPI.getMovieTrailers(movieId)
        }

    override suspend fun getMovieReviews(movieId: Int?): Resource<PaginatedResponse<Review>> =
        safeApiCall(dispatcher) {
            movieAPI.getMovieReviews(movieId)
        }

    override suspend fun getFavoriteMovies(): Resource<ArrayList<Movie>> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun isFavoriteMovie(movieId: Int): Resource<Boolean> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun addFavoriteMovie(movie: Movie): Resource<Unit> =
        throw UnsupportedOperationException("Operation is not available!")

    override suspend fun removeFavoriteMovie(movieId: Int): Resource<Unit> =
        throw UnsupportedOperationException("Operation is not available!")
}