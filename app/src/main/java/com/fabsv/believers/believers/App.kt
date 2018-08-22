package com.fabsv.believers.believers

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper

class App : Application() {

    fun getAppPreferencesHelper(): AppPreferencesHelper {
        return AppPreferencesHelper.getInstance(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}