package com.icerrate.popularmovies.view.common;

/**
 * Created by Ivan Cerrate
 */
public interface BaseCallback<T> {

    void onSuccess(T t);

    void onFailure(String errorMessage);
}