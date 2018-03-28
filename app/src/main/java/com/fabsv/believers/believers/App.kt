package com.fabsv.believers.believers

import android.app.Application
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper

class App : Application() {

    fun getAppPreferencesHelper(): AppPreferencesHelper {
        return AppPreferencesHelper.getInstance(this)
    }
}