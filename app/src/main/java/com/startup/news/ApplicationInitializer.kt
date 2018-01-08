package com.startup.news

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        instance = this
    }
}
