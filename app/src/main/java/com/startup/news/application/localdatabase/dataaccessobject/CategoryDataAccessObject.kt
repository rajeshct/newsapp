package com.startup.news.localdatabase.dataaccessobject

import android.arch.persistence.room.*
import com.startup.news.application.localdatabase.tables.CategoryData
import io.reactivex.Single


/**
 * Created by admin on 11/17/2017.
 */
@Dao
interface CategoryDataAccessObject {

    @Query("SELECT * FROM CategoryData where id= :title")
    fun getAll(title: String): Single<List<CategoryData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: CategoryData)

    @Delete
    fun delete(user: CategoryData)

}