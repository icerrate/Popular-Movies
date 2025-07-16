package com.icerrate.popularmovies.view.movies.catalog

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.source.MovieRepository
import com.icerrate.popularmovies.view.common.BaseCallback

/**
 * @author Ivan Cerrate.
 */
class MoviesCatalogPresenter(
    private val view: MoviesCatalogContract.View,
    private val movieRepository: MovieRepository
) : MoviesCatalogContract.Presenter {

    enum class SortType {
        MOST_POPULAR,
        HIGHEST_RATED,
        FAVORITE
    }

    private var sortType = SortType.MOST_POPULAR
    private var moviesPaginatedResponse = PaginatedResponse<Movie>()

    override fun loadMovies() {
        if (moviesPaginatedResponse.page == 0) {
            refreshMovies()
        } else {
            view.resetMovies()
            view.showMovies(moviesPaginatedResponse.results)
        }
    }

    override fun loadMoviesBySortType(sortType: SortType) {
        this.sortType = sortType
        view.showProgressBar(true)
        loadMovies()
    }

    override fun loadNextMoviesPage() {
        view.showFooterProgress(true)
        val isLastPage = moviesPaginatedResponse.isLastPage
        if (isLastPage) {
            view.showFooterProgress(false)
        } else {
            getMovies(false)
        }
    }

    override fun refreshMovies() {
        moviesPaginatedResponse = PaginatedResponse()
        getMovies(true)
    }

    override fun refreshMoviesBySortType(sortType: SortType) {
        this.sortType = sortType
        resetMovies()
        view.showProgressBar(true)
        getMovies(true)
    }

    override fun onBackFromDetail() {
        if (sortType == SortType.FAVORITE) {
            refreshMovies()
        }
    }

    private fun getMovies(force: Boolean) {
        when (sortType) {
            SortType.MOST_POPULAR -> getInternalPopularMovies(force)
            SortType.HIGHEST_RATED -> getInternalTopRatedMovies(force)
            SortType.FAVORITE -> getInternalFavoriteMovies(force)
        }
    }

    private fun getInternalPopularMovies(force: Boolean) {
        val page = moviesPaginatedResponse.page?.let { it + 1 } ?: 1
        val currentPage = if (force) 1 else page
        
        movieRepository.getPopularMovies(currentPage, object : BaseCallback<PaginatedResponse<Movie>> {
            override fun onSuccess(result: PaginatedResponse<Movie>) {
                if (force) {
                    resetMovies()
                }
                moviesPaginatedResponse.setMeta(
                    result.page,
                    result.totalResults,
                    result.totalPages
                )
                moviesPaginatedResponse.results.addAll(result.results)
                showMovies(result.results)
                finishLoading()
            }

            override fun onFailure(errorMessage: String) {
                view.showError(errorMessage)
                finishLoading()
            }
        })
    }

    private fun getInternalTopRatedMovies(force: Boolean) {
        val page = moviesPaginatedResponse.page?.let { it + 1 } ?: 1
        val currentPage = if (force) 1 else page
        
        movieRepository.getTopRatedMovies(currentPage, object : BaseCallback<PaginatedResponse<Movie>> {
            override fun onSuccess(result: PaginatedResponse<Movie>) {
                if (force) {
                    resetMovies()
                }
                moviesPaginatedResponse.setMeta(
                    result.page,
                    result.totalResults,
                    result.totalPages
                )
                moviesPaginatedResponse.results.addAll(result.results)
                showMovies(result.results)
                finishLoading()
            }

            override fun onFailure(errorMessage: String) {
                view.showError(errorMessage)
                finishLoading()
            }
        })
    }

    private fun getInternalFavoriteMovies(force: Boolean) {
        movieRepository.getFavoriteMovies(object : BaseCallback<ArrayList<Movie>> {
            override fun onSuccess(result: ArrayList<Movie>) {
                if (force) {
                    resetMovies()
                }
                moviesPaginatedResponse.setMeta(PAGINATION, result.size, PAGINATION)
                moviesPaginatedResponse.results.addAll(result)
                showMovies(result)
                finishLoading()
            }

            override fun onFailure(errorMessage: String) {
                view.showError(errorMessage)
                finishLoading()
            }
        })
    }

    private fun showMovies(movies: ArrayList<Movie>) {
        if (movies.isNotEmpty()) {
            view.showNoDataView(false)
            view.showMovies(movies)
        } else {
            if (moviesPaginatedResponse.results.isEmpty()) {
                view.showNoDataView(true)
            }
        }
    }

    private fun finishLoading() {
        view.showFooterProgress(false)
        view.showProgressBar(false)
        view.showRefreshLayout(false)
    }

    private fun resetMovies() {
        moviesPaginatedResponse = PaginatedResponse()
        view.resetMovies()
    }

    override fun onMovieItemClick(movie: Movie) {
        movieRepository.isFavoriteMovie(movie.id, object : BaseCallback<Boolean> {
            override fun onSuccess(result: Boolean) {
                movie.isFavorite = result
                view.goToMovieDetail(movie)
            }

            override fun onFailure(errorMessage: String) {
                view.showError(errorMessage)
            }
        })
    }

    override fun getSortType(): SortType = sortType

    override fun getMoviesPaginatedResponse(): PaginatedResponse<Movie>? = moviesPaginatedResponse

    override fun loadPresenterState(sortType: SortType, response: PaginatedResponse<Movie>?) {
        this.sortType = sortType
        this.moviesPaginatedResponse = response ?: PaginatedResponse()
    }

    companion object {
        private const val PAGINATION = 1
    }
}