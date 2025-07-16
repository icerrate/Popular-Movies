package com.icerrate.popularmovies.view.movies.search

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse

data class SearchMoviesState(
    val query: String? = null,
    val movies: List<Movie> = emptyList(),
    val paginatedResponse: PaginatedResponse<Movie> = PaginatedResponse(),
    val isLoading: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val showSearchHint: Boolean = true,
    val showNoDataView: Boolean = false,
    val errorMessage: String? = null,
    val navigateToMovieDetail: Movie? = null
)