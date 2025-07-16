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

interface MovieDataSource {

    suspend fun getPopularMovies(page: Int?): Resource<PaginatedResponse<Movie>>

    suspend fun getTopRatedMovies(page: Int?): Resource<PaginatedResponse<Movie>>

    suspend fun searchMovies(query: String, page: Int?): Resource<PaginatedResponse<Movie>>

    suspend fun getMovieTrailers(movieId: Int?): Resource<TrailersResponse<Trailer>>

    suspend fun getMovieReviews(movieId: Int?): Resource<PaginatedResponse<Review>>

    suspend fun getFavoriteMovies(): Resource<ArrayList<Movie>>

    suspend fun isFavoriteMovie(movieId: Int): Resource<Boolean>

    suspend fun addFavoriteMovie(movie: Movie): Resource<Unit>

    suspend fun removeFavoriteMovie(movieId: Int): Resource<Unit>
}