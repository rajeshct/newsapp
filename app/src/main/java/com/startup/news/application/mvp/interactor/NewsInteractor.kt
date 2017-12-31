package com.startup.news.application.mvp.interactor

import com.startup.news.application.constants.AppConstants
import com.startup.news.application.network.RetrofitClient
import com.startup.news.application.preaparedata.SuccessResponseWithData.sendCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by admin on 12/27/2017.
 */
class NewsInteractor {

    interface IErrorSuccessCallback {
        fun onSuccess(any: Any?)
        fun onError(message: String)
    }

    fun fetchNewsWithSource(iErrorSuccessCallback: IErrorSuccessCallback?, inputParam: MutableMap<String, String>): Disposable {
        val apis = RetrofitClient.instance
        return apis.fetchTopHeadlines(inputParam)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ kamTeamPoJoGenericResponseModel ->
                    sendCallback(iErrorSuccessCallback, kamTeamPoJoGenericResponseModel)
                }, {
                    iErrorSuccessCallback?.onError(AppConstants.SHOW_COMMON_MESSAGE)
                })
    }

    fun searchNews(iErrorSuccessCallback: IErrorSuccessCallback?, inputParam: MutableMap<String, String>): Disposable {

        val apis = RetrofitClient.instance
        return apis.searchNews(inputParam)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ kamTeamPoJoGenericResponseModel ->
                    sendCallback(iErrorSuccessCallback, kamTeamPoJoGenericResponseModel)
                }, {
                    iErrorSuccessCallback?.onError(AppConstants.SHOW_COMMON_MESSAGE)
                })

    }
}