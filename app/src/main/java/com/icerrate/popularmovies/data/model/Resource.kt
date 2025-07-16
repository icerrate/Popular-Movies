package com.icerrate.popularmovies.data.model

sealed class Resource<out V> {
    data class Success<out V>(val value: V) : Resource<V>()
    data class Error(val message: String) : Resource<Nothing>()
}