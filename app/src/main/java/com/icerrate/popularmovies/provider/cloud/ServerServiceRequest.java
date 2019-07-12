package com.icerrate.popularmovies.provider.cloud;

import com.icerrate.popularmovies.view.common.BaseCallback;

import retrofit2.Call;

/**
 * @author Ivan Cerrate.
 */

public class ServerServiceRequest<T> extends ServiceRequest<T> {

    private Call<T> call;

    public ServerServiceRequest(Call<T> call) {
        this.call = call;
    }

    @Override
    void enqueue(BaseCallback<T> callback) {
        call.enqueue(new RetrofitCallback<>(callback));
    }

    @Override
    public void cancel() {
        call.cancel();
    }

}
