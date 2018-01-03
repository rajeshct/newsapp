package com.startup.news.application.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.startup.news.application.model.apimodel.NewsApiResponse


/**
 * Created by admin on 12/30/2017.
 */
class CategoryDataTypeConverter {

    @TypeConverter
    fun stringToMeasurements(json: String): List<NewsApiResponse> {
        val gson = Gson()
        val type = object : TypeToken<List<NewsApiResponse>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun measurementsToString(list: List<NewsApiResponse>): String {
        val gson = Gson()
        val type = object : TypeToken<List<NewsApiResponse>>() {}.type
        return gson.toJson(list, type)
    }

}