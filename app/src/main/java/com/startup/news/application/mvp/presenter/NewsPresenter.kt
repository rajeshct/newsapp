package com.startup.news.application.mvp.presenter

import com.startup.news.application.BuildConfig
import com.startup.news.application.constants.AppConstants
import com.startup.news.application.interfaces.viewcallback.INewsCallback
import com.startup.news.application.interfaces.viewcallback.INewsSource
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.model.apimodel.NewsSourceResponse
import com.startup.news.application.mvp.interactor.NewsInteractor
import com.startup.news.application.prefrences.SharedPrefrenceData

/**
 * Created by admin on 12/27/2017.
 */
class NewsPresenter {
    private var newsInteractor: NewsInteractor = NewsInteractor()

    var newsCallback: INewsCallback? = null
    var newsSourceCallback: INewsSource? = null

    fun initializeCallback(newsCallback: INewsCallback) {
        this.newsCallback = newsCallback
    }

    fun initializeSourceCallback(newsSourceCallback: INewsSource) {
        this.newsSourceCallback = newsSourceCallback
    }

    fun fetchNewsWithSource(source: String) {
        val inputParam = getApiParam()
        inputParam.put(AppConstants.API_SOURCE, source)
        fetchCommonNews(inputParam)
    }
    @Suppress("UNCHECKED_CAST")
    private fun fetchCommonNews(inputParam: MutableMap<String, String>) {
        newsCallback?.showProgress()
        newsInteractor.fetchNewsWithSource(object : NewsInteractor.IErrorSuccessCallback {
            override fun onSuccess(any: Any?) {
                if (any != null && newsCallback != null) {
                    newsCallback?.hideProgress()
                    newsCallback?.newsData(any as MutableList<NewsApiResponse>)
                }
            }

            override fun onError(message: String) {
                if (newsCallback != null) {
                    newsCallback?.noData()
                    newsCallback?.hideProgress()
                    newsCallback?.showMessage(message)
                }
            }
        }, inputParam)
    }

    private fun getApiParam(): kotlin.collections.MutableMap<kotlin.String, kotlin.String> {
        val inputParam = mutableMapOf<String, String>()
        inputParam.put(AppConstants.API_KEY, BuildConfig.APIKEY)
        return inputParam
    }
    @Suppress("UNCHECKED_CAST")
    fun searchNews(search: String) {
        val inputParam = getApiParam()
        inputParam.put(AppConstants.API_SEARCH, search)
        newsCallback?.showProgress()
        newsInteractor.searchNews(object : NewsInteractor.IErrorSuccessCallback {
            override fun onSuccess(any: Any?) {
                if (any != null && newsCallback != null) {
                    newsCallback?.hideProgress()
                    newsCallback?.newsData(any as MutableList<NewsApiResponse>)
                }
            }

            override fun onError(message: String) {
                if (newsCallback != null) {
                    newsCallback?.noData()
                    newsCallback?.hideProgress()
                    newsCallback?.showMessage(message)
                }
            }
        }, inputParam)
    }
    @Suppress("UNCHECKED_CAST")
    fun getSource(address: String) {
        val inputParam = getApiParam()
        inputParam.put(AppConstants.API_COUNTRY, address)
        newsInteractor.fetchCategory(object : NewsInteractor.IErrorSuccessCallback {
            override fun onSuccess(any: Any?) {
                if (any != null && newsSourceCallback != null) {
                    newsSourceCallback?.hasSources(any as MutableList<NewsSourceResponse>)
                }
            }

            override fun onError(message: String) {

            }
        }, inputParam)
    }

    fun fetchRegionalNews() {
        val inputParam = getApiParam()
        inputParam.put(AppConstants.API_CATEGORY, AppConstants.API_GENERAL)
        inputParam.put(AppConstants.API_COUNTRY, SharedPrefrenceData().getUserCountry())
        fetchCommonNews(inputParam)
    }
}