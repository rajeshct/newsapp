package com.startup.news.application.model.apimodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by admin on 1/2/2018.
 */
data class NewsSourceResponse(
        @SerializedName("id")
        @Expose
        var id: String? = null,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("description")
        @Expose
        var description: String? = null,

        @SerializedName("url")
        @Expose
        var url: String? = null,

        @SerializedName("category")
        @Expose
        var category: String? = null,

        @SerializedName("language")
        @Expose
        var language: String? = null,

        @SerializedName("country")
        @Expose
        var country: String? = null
)