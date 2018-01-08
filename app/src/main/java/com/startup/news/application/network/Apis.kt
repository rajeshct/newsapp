package com.startup.news.application.network

import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.model.apimodel.NewsSourceResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by admin on 12/27/2017.
 */
interface Apis {

    @GET("top-headlines")
    fun fetchTopHeadlines(@QueryMap(encoded = true) options: Map<String, String>): Single<GenericResponseModel<NewsApiResponse>>

    @GET("everything")
    fun searchNews(@QueryMap(encoded = true) inputParam: MutableMap<String, String>): Single<GenericResponseModel<NewsApiResponse>>

    @GET("sources")
    fun fetchCategory(@QueryMap(encoded = true) inputParam: MutableMap<String, String>): Single<GenericResponseModel<NewsSourceResponse>>
}