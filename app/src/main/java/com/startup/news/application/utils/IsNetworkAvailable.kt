package com.startup.news.application.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import com.startup.news.ApplicationInitializer

/**
 * Created by rajesh on 31/12/17.
 */
class IsNetworkAvailable {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager? = ApplicationInitializer.instance
                .applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val networks = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    connectivityManager.allNetworks
                } else {
                    null
                }
                networks?.let {
                    var networkInfo: NetworkInfo
                    for (mNetwork in networks) {
                        networkInfo = connectivityManager.getNetworkInfo(mNetwork)
                        if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            } else {
                val info = connectivityManager.allNetworkInfo
                info?.filter { it.state == NetworkInfo.State.CONNECTED }?.forEach { return true }
            }
        }
        return false
    }
}