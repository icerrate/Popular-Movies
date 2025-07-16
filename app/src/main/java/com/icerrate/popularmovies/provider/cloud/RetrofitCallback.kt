package com.icerrate.popularmovies.provider.cloud

import android.util.Log
import com.icerrate.popularmovies.view.common.BaseCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

/**
 * @author Ivan Cerrate.
 */
class RetrofitCallback<T>(private val callback: BaseCallback<T>) : Callback<T> {

    companion object {
        private val TAG = RetrofitCallback::class.java.simpleName
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            callback.onSuccess(response.body()!!)
        } else {
            var errorMessage = "Unknown Error"
            response.errorBody()?.let { errorBody ->
                try {
                    errorMessage = errorBody.string()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            callback.onFailure(errorMessage)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message}")
        when (t) {
            is UnknownHostException -> callback.onFailure("No internet connection")
            else -> callback.onFailure(t.message ?: "Unknown error")
        }
    }
}