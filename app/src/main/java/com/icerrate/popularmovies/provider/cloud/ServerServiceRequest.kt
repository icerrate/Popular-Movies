package com.icerrate.popularmovies.provider.cloud

import com.icerrate.popularmovies.view.common.BaseCallback
import retrofit2.Call

/**
 * @author Ivan Cerrate.
 */
class ServerServiceRequest<T>(private val call: Call<T>) : ServiceRequest<T>() {

    override fun enqueue(callback: BaseCallback<T>) {
        call.enqueue(RetrofitCallback(callback))
    }

    override fun cancel() {
        call.cancel()
    }
}