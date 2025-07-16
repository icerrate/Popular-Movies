package com.icerrate.popularmovies.view.movies.search

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse

/**
 * @author Ivan Cerrate.
 */
interface SearchMoviesContract {

    interface View {

        fun showProgressBar(show: Boolean)

        fun showError(errorMessage: String)

        fun resetMovies()

        fun showMovies(movies: List<Movie>)

        fun showSearchHint(show: Boolean)

        fun showNoDataView(show: Boolean)

        fun showFooterProgress(show: Boolean)

        fun goToMovieDetail(movie: Movie)
    }

    interface Presenter {

        fun searchMovies(query: String)

        fun loadNextMoviesPage()

        fun loadMovies()

        fun onMovieItemClick(movie: Movie)

        fun getQuery(): String?

        fun getMoviesPaginatedResponse(): PaginatedResponse<Movie>?

        fun loadPresenterState(query: String?, moviePaginatedResponse: PaginatedResponse<Movie>?)
    }
}