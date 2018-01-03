package com.startup.news.application.interfaces.databasecallback

import com.startup.news.application.interfaces.viewcallback.IBaseView
import com.startup.news.application.model.apimodel.NewsApiResponse

/**
 * Created by rajesh on 31/12/17.
 */
interface ICategoryRecordCallback: IBaseView {
    fun emptyLocalDatabase()
    fun categoryDataFromDb(data: List<NewsApiResponse>)
}