package com.icerrate.popularmovies.provider.cloud

import com.icerrate.popularmovies.view.common.BaseCallback

/**
 * @author Ivan Cerrate.
 */
abstract class ServiceRequest<T> {
    abstract fun enqueue(callback: BaseCallback<T>)
    abstract fun cancel()
}