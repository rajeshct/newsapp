package com.startup.news.application.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by admin on 12/27/2017.
 */
class GenericResponseModel<T> {

    @SerializedName("status")
    @Expose
    var isSuccess: String = ""

    @SerializedName("code")
    @Expose
    var code: String = ""

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("totalResults")
    @Expose
    var totalResults: String? = null

    @SerializedName("articles")
    @Expose
    var results: List<T>? = null

    @SerializedName("sources")
    @Expose
    var sources: List<T>? = null
}