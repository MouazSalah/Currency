package com.banquemisr.currency.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication private set
    }

    private fun initTimber() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return super.createStackElementTag(element) + " line: " + element.lineNumber
            }
        })
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initTimber()
    }

}