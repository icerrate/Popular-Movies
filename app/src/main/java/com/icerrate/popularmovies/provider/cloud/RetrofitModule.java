package com.icerrate.popularmovies.provider.cloud;

import com.icerrate.popularmovies.BuildConfig;
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Ivan Cerrate.
 */

public class RetrofitModule {

    private static RetrofitModule instance = null;

    protected RetrofitModule() {
        // Exists only to defeat instantiation.
    }

    public static RetrofitModule get() {
        if(instance == null) {
            instance = new RetrofitModule();
        }
        return instance;
    }

    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }


    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String apiKey = BuildConfig.TMD_API_KEY;

                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", apiKey)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .baseUrl(BuildConfig.API_BASE_URL)
                .build();
    }

    public MovieAPI provideMovieAPI() {
        return get().provideRetrofit(get().providesOkHttpClient()).create(MovieAPI.class);
    }
}
