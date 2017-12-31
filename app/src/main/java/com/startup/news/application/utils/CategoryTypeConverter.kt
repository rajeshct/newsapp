package com.startup.news.application.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse


/**
 * Created by admin on 12/30/2017.
 */
class CategoryTypeConverter {

    @TypeConverter
    fun stringToMeasurements(json: String): List<CategoryFirebaseResponse> {
        val gson = Gson()
        val type = object : TypeToken<List<CategoryFirebaseResponse>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun measurementsToString(list: List<CategoryFirebaseResponse>): String {
        val gson = Gson()
        val type = object : TypeToken<List<CategoryFirebaseResponse>>() {}.type
        return gson.toJson(list, type)
    }
}