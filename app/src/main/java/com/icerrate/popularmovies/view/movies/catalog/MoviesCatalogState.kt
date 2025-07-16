package com.icerrate.popularmovies.view.movies.catalog

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse

data class MoviesCatalogState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val sortType: SortType = SortType.MOST_POPULAR,
    val paginatedResponse: PaginatedResponse<Movie> = PaginatedResponse(),
    val showNoDataView: Boolean = false,
    val errorMessage: String? = null,
    val navigateToMovieDetail: Movie? = null
)