package com.icerrate.popularmovies.view.movies.catalog

import com.icerrate.popularmovies.data.model.Movie

sealed class MoviesCatalogIntent {
    object LoadMovies : MoviesCatalogIntent()
    data class LoadMoviesBySortType(val sortType: SortType) : MoviesCatalogIntent()
    object LoadNextMoviesPage : MoviesCatalogIntent()
    object RefreshMovies : MoviesCatalogIntent()
    data class RefreshMoviesBySortType(val sortType: SortType) : MoviesCatalogIntent()
    object OnBackFromDetail : MoviesCatalogIntent()
    data class OnMovieItemClick(val movie: Movie) : MoviesCatalogIntent()
    data class RestoreState(val sortType: SortType, val moviesResponse: com.icerrate.popularmovies.data.model.PaginatedResponse<Movie>?) : MoviesCatalogIntent()
}

enum class SortType {
    MOST_POPULAR,
    HIGHEST_RATED,
    FAVORITE
}