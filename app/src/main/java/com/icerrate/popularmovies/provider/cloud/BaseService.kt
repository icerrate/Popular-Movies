package com.icerrate.popularmovies.provider.cloud

import com.icerrate.popularmovies.view.common.BaseCallback

/**
 * @author Ivan Cerrate.
 */
open class BaseService : BaseBaseService {

    private var serviceRequestList: MutableList<ServiceRequest<*>>? = null

    protected fun <T> enqueue(serviceRequest: ServiceRequest<T>, doctavioCallback: BaseCallback<T>) {
        if (serviceRequestList == null) {
            serviceRequestList = mutableListOf()
        }
        serviceRequestList?.add(serviceRequest)
        serviceRequest.enqueue(object : BaseCallback<T> {
            override fun onSuccess(result: T) {
                dequeue(serviceRequest)
                doctavioCallback.onSuccess(result)
            }

            override fun onFailure(errorMessage: String) {
                dequeue(serviceRequest)
                doctavioCallback.onFailure(errorMessage)
            }
        })
    }

    private fun dequeue(serviceRequest: ServiceRequest<*>) {
        serviceRequestList?.remove(serviceRequest)
    }

    override fun cancelAll() {
        serviceRequestList?.let { list ->
            if (list.isNotEmpty()) {
                list.forEach { it.cancel() }
                list.clear()
            }
        }
    }
}