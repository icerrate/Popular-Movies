package com.icerrate.popularmovies.provider.cloud;

import android.util.Log;

import com.icerrate.popularmovies.view.common.BaseCallback;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ivan Cerrate
 */
public class RetrofitCallback<T> implements Callback<T>  {

    private static final String TAG = RetrofitCallback.class.getSimpleName();

    private BaseCallback<T> callback;

    public RetrofitCallback(BaseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            callback.onSuccess(response.body());
        } else {
            String errorMessage = "Unknown Error";
            if (response.errorBody() != null) {
                try {
                    errorMessage = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            callback.onFailure(errorMessage);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage());
        callback.onFailure(t.getMessage());
    }

}