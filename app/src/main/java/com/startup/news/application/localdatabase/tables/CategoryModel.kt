package com.startup.news.localdatabase.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import com.startup.news.application.utils.CategoryTypeConverter

/**
 * Created by admin on 11/17/2017.
 */
@Entity
@TypeConverters(CategoryTypeConverter::class)
class CategoryModel {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 1

    @ColumnInfo(name = "data")
    var data: List<CategoryFirebaseResponse>? = null

}
