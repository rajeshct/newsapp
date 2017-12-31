package com.startup.news.application.network

import com.google.gson.GsonBuilder
import com.startup.news.application.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by admin on 12/27/2017.
 */
class RetrofitClient {

    private object Holder {
        fun getRetrofitClient(): Apis {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            val retrofitInstance = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASEURL)
                    .client(httpClient.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            return retrofitInstance.create(Apis::class.java)
        }
    }

    companion object {
        val instance: Apis by lazy { Holder.getRetrofitClient() }
    }

}