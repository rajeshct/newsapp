package com.startup.news.application.localdatabase.databaseoperation

import com.startup.news.application.interfaces.databasecallback.ICategoryRecordCallback
import com.startup.news.application.localdatabase.tables.CategoryData
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.localdatabase.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.internal.operators.completable.CompletableFromAction
import io.reactivex.schedulers.Schedulers

/**
 * Created by rajesh on 31/12/17.
 */
class CategoryRecordDatabaseOperation {

    fun insertCategoryRecord(categoryModel: CategoryFirebaseResponse, data: MutableList<NewsApiResponse>,
                             iDatabaseSuccessFailureCallback: ICategoryRecordCallback?) {
        CompletableFromAction(Action {
            val categoryData = CategoryData(categoryModel.title)
            categoryData.data = data
            AppDatabase.instance.categoryDataDao().insert(categoryData)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ iDatabaseSuccessFailureCallback?.showMessage("") },
                        { iDatabaseSuccessFailureCallback?.showMessage("") })
    }

    fun getSelectedCategoryRecords(title: String, iHomeDatabaseCallback: ICategoryRecordCallback) {
        AppDatabase.instance.categoryDataDao().getAll(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    if (!it.isEmpty())
                        iHomeDatabaseCallback.categoryDataFromDb(it[0].data)
                    else
                        iHomeDatabaseCallback.emptyLocalDatabase()
                }, { iHomeDatabaseCallback.emptyLocalDatabase() })
    }

}