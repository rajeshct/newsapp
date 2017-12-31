package com.startup.news.application.interfaces.databasecallback

import com.startup.news.localdatabase.tables.CategoryModel

/**
 * Created by admin on 12/27/2017.
 */
interface IDatabaseSuccessFailureCallback {
    fun showMessage(message: String)
    fun categoryData(categoryModel: List<CategoryModel>)
}