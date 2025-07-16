package com.icerrate.popularmovies.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Ivan Cerrate
 */

@Parcelize
data class Review(
    val id: String,
    val author: String,
    val content: String,
    val url: String
) : Parcelable