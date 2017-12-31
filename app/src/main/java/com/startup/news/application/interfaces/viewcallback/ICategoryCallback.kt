package com.startup.news.application.interfaces.viewcallback

import com.startup.news.application.model.apimodel.CategoryFirebaseResponse

/**
 * Created by rajesh on 26/12/17.
 */
interface ICategoryCallback : IBaseView {
    fun categories(tempData: List<CategoryFirebaseResponse>)
}