package com.icerrate.popularmovies.view.movies.catalog

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.view.common.BaseView
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogPresenter.SortType

/**
 * @author Ivan Cerrate.
 */
interface MoviesCatalogContract {

    interface View : BaseView {

        fun resetMovies()

        fun showMovies(movies: ArrayList<Movie>)

        fun showNoDataView(show: Boolean)

        fun showFooterProgress(show: Boolean)

        fun goToMovieDetail(movie: Movie)

        fun showProgressBar(show: Boolean)

        fun showRefreshLayout(show: Boolean)
    }

    interface Presenter {

        fun loadMovies()

        fun loadMoviesBySortType(sortType: SortType)

        fun loadNextMoviesPage()

        fun refreshMovies()

        fun refreshMoviesBySortType(sortType: SortType)

        fun onBackFromDetail()

        fun onMovieItemClick(movie: Movie)

        fun getSortType(): SortType

        fun getMoviesPaginatedResponse(): PaginatedResponse<Movie>?

        fun loadPresenterState(sortType: SortType, response: PaginatedResponse<Movie>?)
    }
}