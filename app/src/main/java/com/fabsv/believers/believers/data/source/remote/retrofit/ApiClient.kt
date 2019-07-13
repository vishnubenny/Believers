package com.fabsv.believers.believers.data.source.remote.retrofit

import com.fabsv.believers.believers.util.constants.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
    companion object {
        private val BASE_URL = AppConstants.ApiConstants.BASE_URL
        private var retrofit: Retrofit? = null

        var okHttpClient = OkHttpClient.Builder()
                .connectTimeout(/*2, TimeUnit.MINUTES*/ 5, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.MINUTES /*5, TimeUnit.SECONDS*/)
                .writeTimeout(2, TimeUnit.MINUTES /*5, TimeUnit.SECONDS*/)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

        fun getClient(): Retrofit? {
            if (null == retrofit) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build()
            }
            return retrofit
        }
    }
}