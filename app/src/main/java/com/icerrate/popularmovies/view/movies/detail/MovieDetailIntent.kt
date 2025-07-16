package com.icerrate.popularmovies.view.movies.detail

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer

sealed class MovieDetailIntent {
    data class SetMovieDetail(val movie: Movie) : MovieDetailIntent()
    object LoadMovieDetails : MovieDetailIntent()
    object OnFavoriteFabClicked : MovieDetailIntent()
    object OnShareClick : MovieDetailIntent()
    object ValidateMenu : MovieDetailIntent()
    data class RestoreState(val movie: Movie?, val trailers: ArrayList<Trailer>?, val reviews: ArrayList<Review>?) : MovieDetailIntent()
}