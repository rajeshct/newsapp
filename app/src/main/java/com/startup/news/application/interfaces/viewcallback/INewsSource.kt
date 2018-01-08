package com.startup.news.application.interfaces.viewcallback

import com.startup.news.application.model.apimodel.NewsSourceResponse

/**
 * Created by admin on 1/2/2018.
 */
interface INewsSource {
    fun hasSources(data: MutableList<NewsSourceResponse>)
}