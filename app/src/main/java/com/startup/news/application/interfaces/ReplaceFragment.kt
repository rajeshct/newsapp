package com.startup.com.newsapplication.interfaces

import android.support.v4.app.Fragment


/**
 * Created by admin on 11/17/2017.
 */
interface ReplaceFragment {
    fun replaceFragment(fragment: Fragment, tag: String)
    fun finishSelf()
}