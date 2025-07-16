package com.icerrate.popularmovies.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Ivan Cerrate
 */

@Parcelize
data class PaginatedResponse<T : Parcelable>(
    var page: Int? = 0,
    @SerializedName("total_results")
    var totalResults: Int? = null,
    @SerializedName("total_pages")
    var totalPages: Int? = null,
    val results: ArrayList<T> = ArrayList()
) : Parcelable {

    val isLastPage: Boolean
        get() = page != null && totalPages != null && page == totalPages

    fun setMeta(page: Int?, totalResults: Int?, totalPages: Int?) {
        this.page = page
        this.totalResults = totalResults
        this.totalPages = totalPages
    }
}