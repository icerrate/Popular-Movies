package com.icerrate.popularmovies.view.movies.search

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse

sealed class SearchMoviesIntent {
    data class SearchMovies(val query: String) : SearchMoviesIntent()
    object LoadNextMoviesPage : SearchMoviesIntent()
    object LoadMovies : SearchMoviesIntent()
    data class OnMovieItemClick(val movie: Movie) : SearchMoviesIntent()
    data class RestoreState(val query: String?, val moviePaginatedResponse: PaginatedResponse<Movie>?) : SearchMoviesIntent()
}