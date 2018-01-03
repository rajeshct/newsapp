package com.startup.news.application.localdatabase.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.utils.CategoryDataTypeConverter

/**
 * Created by rajesh on 31/12/17.
 */
@Entity
@TypeConverters(CategoryDataTypeConverter::class)
class CategoryData(@PrimaryKey
                   @ColumnInfo(name = "id")
                   var id: String) {

    @ColumnInfo(name = "data")
    lateinit var data: List<NewsApiResponse>
}