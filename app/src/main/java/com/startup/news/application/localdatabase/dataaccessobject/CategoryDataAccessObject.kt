package com.startup.news.localdatabase.dataaccessobject

import android.arch.persistence.room.*
import com.startup.news.localdatabase.tables.CategoryModel
import io.reactivex.Single


/**
 * Created by admin on 11/17/2017.
 */
@Dao
interface CategoryDataAccessObject {

    @Query("SELECT * FROM CategoryModel")
    fun getAll(): Single<List<CategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: CategoryModel)

    @Delete
    fun delete(user: CategoryModel)

}