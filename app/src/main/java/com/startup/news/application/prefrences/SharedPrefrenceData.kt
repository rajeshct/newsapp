package com.startup.news.application.prefrences

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.startup.news.ApplicationInitializer

/**
 * Created by rajesh on 26/11/17.
 */

class SharedPrefrenceData {

    private fun getSharePrefrence(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ApplicationInitializer.instance.applicationContext)
    }

    fun getSharedPrefrenceEditor(): SharedPreferences.Editor {
        return getSharePrefrence().edit()
    }

    fun isCategorySelected(): Boolean {
        return getSharePrefrence().getBoolean(PrefrenceConstants.IS_CATEGORY_SELECTED, false)
    }

    fun setCategorySelected() {
        getSharedPrefrenceEditor().putBoolean(PrefrenceConstants.IS_CATEGORY_SELECTED, true).apply()
    }


}
