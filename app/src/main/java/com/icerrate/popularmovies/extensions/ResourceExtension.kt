package com.icerrate.popularmovies.extensions

import com.icerrate.popularmovies.data.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response


suspend inline fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () -> Response<T>
): Resource<T> {
    return withContext(dispatcher) {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: response.message()
                Resource.Error(errorMsg ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "")
        }
    }
}