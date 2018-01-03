package com.startup.news.application.firebaseoperations

import bestwishes.startup.com.wishes.firebase.FireBaseDocumentId
import bestwishes.startup.com.wishes.firebase.FireBaseFieldId
import bestwishes.startup.com.wishes.firebase.FirebaseCollection
import com.google.firebase.firestore.FirebaseFirestore
import com.startup.news.application.constants.AppConstants
import com.startup.news.application.interfaces.viewcallback.ICategoryCallback
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by rajesh on 9/12/17.
 */
class FireBaseGetData private constructor() {
    private val db = FirebaseFirestore.getInstance()

    private object Holder {
        val INSTANCE = synchronized(FireBaseGetData::class.java) {
            FireBaseGetData()
        }
    }

    companion object {
        val instance: FireBaseGetData by lazy { Holder.INSTANCE }
    }

    @Suppress("UNCHECKED_CAST")
    fun getDataFromFireBase(homeDataCallback: ICategoryCallback?) {
        homeDataCallback?.showProgress()
        db.collection(FirebaseCollection.CATEGORY)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            if (document.id == FireBaseDocumentId.SECTION) {
                                val dataTemp: MutableList<HashMap<String, Any>> = document.data[FireBaseFieldId.DATA]
                                        as MutableList<HashMap<String, Any>>
                                parseDataAsync(dataTemp, homeDataCallback)
                                break
                            }
                        }
                    } else {
                        homeDataCallback?.hideProgress()
                        homeDataCallback?.noData()
                    }
                }
    }

    private fun parseDataAsync(dataTemp: MutableList<HashMap<String, Any>>, homeDataCallback: ICategoryCallback?) {

        Single.create<MutableList<CategoryFirebaseResponse>> {
            val data = mutableListOf<CategoryFirebaseResponse>()
            dataTemp.reverse()
            dataTemp.mapTo(data) {
                CategoryFirebaseResponse(title = it[FireBaseFieldId.TITLE] as String, source = it[FireBaseFieldId.SOURCE] as String)
            }
            it.onSuccess(data)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    homeDataCallback?.hideProgress()
                    homeDataCallback?.categories(it)
                }) {
                    homeDataCallback?.hideProgress()
                    homeDataCallback?.showMessage(AppConstants.COMMON_ERROR)
                }
    }
}