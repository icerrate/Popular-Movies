package com.icerrate.popularmovies.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.icerrate.popularmovies.BuildConfig
import kotlinx.parcelize.Parcelize

/**
 * @author Ivan Cerrate
 */

@Parcelize
data class Movie(
    val id: Int,
    @SerializedName("vote_count")
    val voteCount: Int = 0,
    @SerializedName("video")
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    val title: String = "",
    val popularity: Double = 0.0,
    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("original_language")
    val originalLanguage: String = "",
    @SerializedName("original_title")
    val originalTitle: String = "",
    @SerializedName("genre_ids")
    val genreIds: ArrayList<Int> = ArrayList(),
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    val adult: Boolean = false,
    val overview: String = "",
    @SerializedName("release_date")
    val releaseDate: String = "",
    var isFavorite: Boolean = false
) : Parcelable {

    fun getPosterUrl(size: String): String {
        return BuildConfig.IMAGES_BASE_URL + size + posterPath
    }

    fun getBackdropUrl(size: String): String {
        return BuildConfig.IMAGES_BASE_URL + size + backdropPath
    }
}