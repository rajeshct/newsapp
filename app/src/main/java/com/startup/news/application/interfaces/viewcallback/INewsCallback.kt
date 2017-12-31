package com.startup.news.application.interfaces.viewcallback

import com.startup.news.application.model.apimodel.NewsApiResponse

/**
 * Created by rajesh on 27/12/17.
 */
interface INewsCallback : IBaseView {
    fun newsData(data: MutableList<NewsApiResponse>)
}