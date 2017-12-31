package com.startup.news.application.model.apimodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by admin on 12/27/2017.
 */
data class NewsApiResponse(
        @SerializedName("author")
        @Expose
        var author: String?,

        @SerializedName("title")
        @Expose
        var title: String?,

        @SerializedName("description")
        @Expose
        var description: String?,

        @SerializedName("url")
        @Expose
        var url: String?,

        @SerializedName("urlToImage")
        @Expose
        var urlToImage: String?,

        @SerializedName("publishedAt")
        @Expose
        var publishedAt: String?
)