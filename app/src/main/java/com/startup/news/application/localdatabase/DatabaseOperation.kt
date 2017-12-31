package com.startup.news.application.localdatabase

import com.startup.news.application.interfaces.databasecallback.IDatabaseSuccessFailureCallback
import com.startup.news.localdatabase.AppDatabase
import com.startup.news.localdatabase.tables.CategoryModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.internal.operators.completable.CompletableFromAction
import io.reactivex.schedulers.Schedulers

/**
 * Created by admin on 12/27/2017.
 */
class DatabaseOperation {

    fun insertCategory(categoryModel: CategoryModel, iDatabaseSuccessFailureCallback: IDatabaseSuccessFailureCallback?) {
        CompletableFromAction(Action {
            AppDatabase.instance.userDao().insert(categoryModel)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ iDatabaseSuccessFailureCallback?.showMessage("") },
                        { iDatabaseSuccessFailureCallback?.showMessage("") })
    }

    fun getSelectedCategory(iHomeDatabaseCallback: IDatabaseSuccessFailureCallback) {
        AppDatabase.instance.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    iHomeDatabaseCallback.categoryData(it)
                }, { iHomeDatabaseCallback.showMessage("") })
    }
}