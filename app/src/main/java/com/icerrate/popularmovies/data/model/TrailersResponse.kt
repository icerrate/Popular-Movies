package com.icerrate.popularmovies.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Ivan Cerrate
 */

@Parcelize
data class TrailersResponse<T : Parcelable>(
    val id: Int? = 0,
    val results: ArrayList<T> = ArrayList()
) : Parcelable {

    fun getPage(): Int? = id
}