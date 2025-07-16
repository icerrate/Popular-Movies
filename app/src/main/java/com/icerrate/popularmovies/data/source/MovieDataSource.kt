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

interface MovieDataSource {

    fun getPopularMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>)

    fun getTopRatedMovies(page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>)

    fun searchMovies(query: String, page: Int?, callback: BaseCallback<PaginatedResponse<Movie>>)

    fun getMovieTrailers(movieId: Int?, callback: BaseCallback<TrailersResponse<Trailer>>)

    fun getMovieReviews(movieId: Int?, callback: BaseCallback<PaginatedResponse<Review>>)

    fun getFavoriteMovies(callback: BaseCallback<ArrayList<Movie>>)

    fun isFavoriteMovie(movieId: Int, callback: BaseCallback<Boolean>)

    fun addFavoriteMovie(movie: Movie, callback: BaseCallback<Unit>)

    fun removeFavoriteMovie(movieId: Int, callback: BaseCallback<Unit>)
}