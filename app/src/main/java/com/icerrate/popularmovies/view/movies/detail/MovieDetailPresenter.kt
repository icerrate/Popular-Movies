package com.icerrate.popularmovies.view.movies.detail

import com.icerrate.popularmovies.R
import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.PaginatedResponse
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.data.model.TrailersResponse
import com.icerrate.popularmovies.data.source.MovieRepository
import com.icerrate.popularmovies.utils.FormatUtils
import com.icerrate.popularmovies.view.common.BaseCallback

/**
 * @author Ivan Cerrate.
 */
class MovieDetailPresenter(
    private val view: MovieDetailContract.View,
    private val movieRepository: MovieRepository
) : MovieDetailContract.Presenter {

    private var movie: Movie? = null
    private var trailers: ArrayList<Trailer>? = null
    private var reviews: ArrayList<Review>? = null

    override fun setMovieDetail(movieDetail: Movie) {
        this.movie = movieDetail
    }

    override fun loadMovieDetails() {
        loadHeader()
        loadTrailers()
        loadReviews()
    }

    private fun loadHeader() {
        movie?.let { movie ->
            val title = movie.title
            val releaseDate = FormatUtils.formatDate(
                movie.releaseDate,
                FormatUtils.FORMAT_YYYY_MM_DD,
                FormatUtils.FORMAT_MMM_DD_YYYY
            )
            val posterUrl = movie.getPosterUrl(POSTER_CODE)
            val backdropUrl = movie.getBackdropUrl(BACKDROP_CODE)
            val rating = "%.1f".format(movie.voteAverage)
            val synopsis = movie.overview
            val isFavorite = movie.isFavorite
            
            view.showHeader(title, releaseDate, posterUrl, backdropUrl, rating, synopsis)
            view.showFavoriteState(isFavorite)
        }
    }

    private fun loadTrailers() {
        if (trailers == null) {
            getInternalTrailers()
        } else {
            showTrailers(trailers!!)
        }
    }

    private fun getInternalTrailers() {
        movie?.let { movie ->
            movieRepository.getMovieTrailers(movie.id, object : BaseCallback<TrailersResponse<Trailer>> {
                override fun onSuccess(response: TrailersResponse<Trailer>) {
                    trailers = response.results
                    showTrailers(trailers!!)
                }

                override fun onFailure(errorMessage: String) {
                    view.showError(errorMessage)
                    view.showShareMenu(false)
                    view.showTrailersNoData(true)
                }
            })
        }
    }

    private fun showTrailers(trailers: ArrayList<Trailer>) {
        if (trailers.isNotEmpty()) {
            view.showTrailersNoData(false)
            view.showTrailers(trailers)
            view.showShareMenu(true)
        } else {
            view.showTrailersNoData(true)
            view.showShareMenu(false)
        }
    }

    private fun loadReviews() {
        if (reviews == null) {
            getInternalReviews()
        } else {
            showReviews(reviews!!)
        }
    }

    private fun getInternalReviews() {
        movie?.let { movie ->
            movieRepository.getMovieReviews(movie.id, object : BaseCallback<PaginatedResponse<Review>> {
                override fun onSuccess(response: PaginatedResponse<Review>) {
                    reviews = response.results
                    showReviews(reviews!!)
                }

                override fun onFailure(errorMessage: String) {
                    view.showError(errorMessage)
                    view.showReviewsNoData(true)
                }
            })
        }
    }

    private fun showReviews(reviews: ArrayList<Review>) {
        if (reviews.isNotEmpty()) {
            view.showReviewsNoData(false)
            view.showReviews(reviews)
        } else {
            view.showReviewsNoData(true)
        }
    }

    override fun onFavoriteFabClicked() {
        movie?.let { movie ->
            val isFavorite = movie.isFavorite
            if (isFavorite) {
                movieRepository.removeFavoriteMovie(movie.id, object : BaseCallback<Unit> {
                    override fun onSuccess(result: Unit) {
                        movie.isFavorite = false
                        view.updateFavoriteState(false)
                        view.showSnackbarMessage(R.string.favorite_removed)
                    }

                    override fun onFailure(errorMessage: String) {
                        view.showError(errorMessage)
                    }
                })
            } else {
                movieRepository.addFavoriteMovie(movie, object : BaseCallback<Unit> {
                    override fun onSuccess(result: Unit) {
                        movie.isFavorite = true
                        view.updateFavoriteState(true)
                        view.showSnackbarMessage(R.string.favorite_added)
                    }

                    override fun onFailure(errorMessage: String) {
                        view.showError(errorMessage)
                    }
                })
            }
        }
    }

    override fun onShareClick() {
        val hasTrailers = trailers?.isNotEmpty() == true
        if (hasTrailers) {
            trailers?.firstOrNull()?.let {
                view.prepareTrailerShare(it.getVideoUrl())
            }
        }
    }

    override fun validateMenu() {
        val hasTrailers = trailers?.isNotEmpty() == true
        view.showShareMenu(hasTrailers)
    }

    // Presenter State
    override fun getMovie(): Movie? = movie

    override fun getTrailers(): ArrayList<Trailer>? = trailers

    override fun getReviews(): ArrayList<Review>? = reviews

    override fun loadPresenterState(movie: Movie?, trailers: ArrayList<Trailer>?, reviews: ArrayList<Review>?) {
        this.movie = movie
        this.trailers = trailers
        this.reviews = reviews
    }

    companion object {
        private const val POSTER_CODE = "w342"
        private const val BACKDROP_CODE = "w780"
    }
}