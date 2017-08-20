package com.icerrate.popularmovies.provider.cloud;

import com.icerrate.popularmovies.view.common.BaseCallback;

import retrofit2.Call;

/**
 * Created by Ivan Cerrate
 */
public class ServerServiceRequest<T> extends ServiceRequest<T> {

    private Call<T> call;

    public ServerServiceRequest(Call<T> call) {
        this.call = call;
    }

    @Override
    void enqueue(BaseCallback<T> baseCallback) {
        call.enqueue(new RetrofitCallback<>(baseCallback));
    }

    @Override
    public void cancel() {
        call.cancel();
    }

}
