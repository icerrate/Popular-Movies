package com.icerrate.popularmovies.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Ivan Cerrate
 */

@Parcelize
data class Trailer(
    val id: String,
    @SerializedName("iso_639_1")
    val iso639: String,
    @SerializedName("iso_3166_1")
    val iso3166: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
) : Parcelable {

    fun getVideoUrl(): String {
        return "https://www.youtube.com/watch?v=$key"
    }

    fun getVideoThumbnail(): String {
        return "https://img.youtube.com/vi/$key/mqdefault.jpg"
    }
}