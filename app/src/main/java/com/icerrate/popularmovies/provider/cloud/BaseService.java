package com.icerrate.popularmovies.provider.cloud;

import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Cerrate.
 */

public class BaseService implements BaseBaseService {

    private List<ServiceRequest> serviceRequestList;

    protected <T> void enqueue(final ServiceRequest<T> serviceRequest, final BaseCallback<T> doctavioCallback) {
        if (serviceRequestList == null) {
            serviceRequestList = new ArrayList<>();
        }
        serviceRequestList.add(serviceRequest);
        serviceRequest.enqueue(new BaseCallback<T>() {
            @Override
            public void onSuccess(T t) {
                dequeue(serviceRequest);
                doctavioCallback.onSuccess(t);
            }

            @Override
            public void onFailure(String errorMessage) {
                dequeue(serviceRequest);
                doctavioCallback.onFailure(errorMessage);
            }
        });
    }

    protected void dequeue(ServiceRequest serviceRequest) {
        serviceRequestList.remove(serviceRequest);
    }

    @Override
    public void cancelAll() {
        if (serviceRequestList != null && !serviceRequestList.isEmpty()) {
            for (ServiceRequest serviceRequest : serviceRequestList) {
                serviceRequest.cancel();
            }
            serviceRequestList.clear();
        }
    }

}
