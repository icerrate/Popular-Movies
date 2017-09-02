package com.icerrate.popularmovies.provider.cloud;

import com.icerrate.popularmovies.view.common.BaseCallback;

/**
 * @author Ivan Cerrate
 */
public abstract class ServiceRequest<T> {

    abstract void enqueue(BaseCallback<T> doctavioCallback);

    public abstract void cancel();
}
