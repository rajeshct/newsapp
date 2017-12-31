package com.startup.news.application.interfaces.viewcallback

/**
 * Created by rajesh on 26/12/17.
 */
interface IBaseView {
    fun showProgress()
    fun hideProgress()
    fun showMessage(message: String)
    fun noData()
}