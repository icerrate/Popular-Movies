package com.icerrate.popularmovies.provider.cloud

import com.icerrate.popularmovies.BuildConfig
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Ivan Cerrate.
 */
class RetrofitModule private constructor() {

    companion object {
        @Volatile
        private var instance: RetrofitModule? = null

        fun get(): RetrofitModule {
            return instance ?: synchronized(this) {
                instance ?: RetrofitModule().also { instance = it }
            }
        }
    }

    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val builder = okHttpClient.newBuilder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.writeTimeout(60, TimeUnit.SECONDS)
        builder.addInterceptor { chain: Interceptor.Chain ->
            val apiKey = BuildConfig.TMD_API_KEY
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .baseUrl(BuildConfig.API_BASE_URL)
            .build()
    }

    fun provideMovieAPI(): MovieAPI {
        return get().provideRetrofit(get().providesOkHttpClient()).create(MovieAPI::class.java)
    }
}