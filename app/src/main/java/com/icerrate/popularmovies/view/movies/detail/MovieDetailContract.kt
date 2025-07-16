package com.icerrate.popularmovies.view.movies.detail

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer
import com.icerrate.popularmovies.view.common.BaseView

/**
 * @author Ivan Cerrate.
 */
interface MovieDetailContract {

    interface View : BaseView {

        fun showHeader(
            title: String,
            releaseDate: String,
            posterUrl: String,
            backdropUrl: String,
            rating: String,
            synopsis: String
        )

        fun showTrailers(trailers: ArrayList<Trailer>)

        fun showTrailersNoData(show: Boolean)

        fun showReviews(reviews: ArrayList<Review>)

        fun showReviewsNoData(show: Boolean)

        fun showFavoriteState(isFavorite: Boolean)

        fun updateFavoriteState(isFavorite: Boolean)

        fun prepareTrailerShare(trailerUrl: String)

        fun showShareMenu(show: Boolean)
    }

    interface Presenter {

        fun setMovieDetail(movieDetail: Movie)

        fun loadMovieDetails()

        fun onFavoriteFabClicked()

        fun onShareClick()

        fun validateMenu()

        fun getMovie(): Movie?

        fun getTrailers(): ArrayList<Trailer>?

        fun getReviews(): ArrayList<Review>?

        fun loadPresenterState(movie: Movie?, trailers: ArrayList<Trailer>?, reviews: ArrayList<Review>?)
    }
}