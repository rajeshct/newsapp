package com.startup.news.application.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by admin on 1/2/2018.
 */
object Utils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}