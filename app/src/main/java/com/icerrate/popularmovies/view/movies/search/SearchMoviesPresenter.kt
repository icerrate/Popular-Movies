package com.icerrate.popularmovies.view.movies.search

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.source.MovieRepository
import com.icerrate.popularmovies.view.common.BaseCallback

class SearchMoviesPresenter(
    private val view: SearchMoviesContract.View,
    private val movieRepository: MovieRepository
) : SearchMoviesContract.Presenter {

    companion object {
        private const val MIN_QUERY_LENGTH = 3
    }

    private var query: String? = null
    private var moviesPaginatedResponse: PaginatedResponse<Movie>? = PaginatedResponse<Movie>()

    override fun searchMovies(query: String) {
        val isValid = query.length >= MIN_QUERY_LENGTH
        if (isValid) {
            view.showProgressBar(true)
            this.query = query
            searchInternalMovies(query, force = true)
        } else {
            resetMovies()
            view.showSearchHint(true)
        }
    }

    override fun loadNextMoviesPage() {
        view.showFooterProgress(true)
        val isLastPage = moviesPaginatedResponse?.isLastPage == true
        if (isLastPage) {
            view.showFooterProgress(false)
        } else {
            query?.let { searchInternalMovies(it, force = false) }
        }
    }

    private fun searchInternalMovies(query: String, force: Boolean) {
        val page = moviesPaginatedResponse?.page?.let { it + 1 } ?: 1
        val currentPage = if (force) 1 else page
        
        movieRepository.searchMovies(query, currentPage, object : BaseCallback<PaginatedResponse<Movie>> {
            override fun onSuccess(response: PaginatedResponse<Movie>) {
                if (force) {
                    resetMovies()
                }
                moviesPaginatedResponse?.setMeta(
                    response.page,
                    response.totalResults,
                    response.totalPages
                )
                moviesPaginatedResponse?.results?.addAll(response.results)
                showMovies(response.results)
                finishLoading()
            }

            override fun onFailure(errorMessage: String) {
                view.showError(errorMessage)
                if (moviesPaginatedResponse?.results?.isEmpty() == true) {
                    view.showNoDataView(true)
                }
                finishLoading()
            }
        })
    }

    override fun loadMovies() {
        if (moviesPaginatedResponse?.page != 0) {
            view.resetMovies()
            view.showMovies(moviesPaginatedResponse?.results ?: emptyList())
        }
    }

    override fun onMovieItemClick(movie: Movie) {
        movieRepository.isFavoriteMovie(movie.id, object : BaseCallback<Boolean> {
            override fun onSuccess(isFavorite: Boolean) {
                movie.isFavorite = isFavorite
                view.goToMovieDetail(movie)
            }

            override fun onFailure(errorMessage: String) {
                view.showError(errorMessage)
            }
        })
    }

    private fun showMovies(movies: ArrayList<Movie>?) {
        if (!movies.isNullOrEmpty()) {
            view.showNoDataView(false)
            view.showMovies(movies)
        } else {
            if (moviesPaginatedResponse?.results?.isEmpty() == true) {
                view.showNoDataView(true)
            }
        }
    }

    private fun finishLoading() {
        view.showFooterProgress(false)
        view.showProgressBar(false)
    }

    private fun resetMovies() {
        moviesPaginatedResponse = PaginatedResponse()
        view.resetMovies()
    }

    // Presenter State
    override fun getQuery(): String? = query

    override fun getMoviesPaginatedResponse(): PaginatedResponse<Movie>? = moviesPaginatedResponse

    override fun loadPresenterState(
        query: String?,
        moviePaginatedResponse: PaginatedResponse<Movie>?
    ) {
        this.query = query
        this.moviesPaginatedResponse = moviePaginatedResponse
    }
}