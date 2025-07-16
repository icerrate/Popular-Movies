package com.icerrate.popularmovies.view.movies.detail

import com.icerrate.popularmovies.data.model.Movie
import com.icerrate.popularmovies.data.model.Review
import com.icerrate.popularmovies.data.model.Trailer

data class MovieDetailState(
    val movie: Movie? = null,
    val trailers: ArrayList<Trailer> = arrayListOf(),
    val reviews: ArrayList<Review> = arrayListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showShareMenu: Boolean = false,
    val shareTrailerUrl: String? = null
)