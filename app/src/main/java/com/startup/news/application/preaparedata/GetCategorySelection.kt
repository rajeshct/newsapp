package com.startup.news.application.preaparedata

import com.startup.news.application.localdatabase.databaseoperation.CategoryDatabaseOperation
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import com.startup.news.application.prefrences.SharedPrefrenceData
import com.startup.news.localdatabase.tables.CategoryModel

/**
 * Created by rajesh on 26/12/17.
 */
class GetCategorySelection {

    fun categorySelection(data: List<CategoryFirebaseResponse>) {
        val dataToSend = data.filter { it.isSelected }
        val categoryModel = CategoryModel()
        categoryModel.data = dataToSend
        CategoryDatabaseOperation().insertCategory(categoryModel, null)
        SharedPrefrenceData().setCategorySelected()
    }

}