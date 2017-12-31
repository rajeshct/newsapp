package com.startup.news

import android.app.Application
import android.content.Context

/**
 * Created by rajesh on 3/12/17.
 */

class ApplicationInitializer : Application() {

    companion object {
        @get:Synchronized
        lateinit var instance: ApplicationInitializer
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
