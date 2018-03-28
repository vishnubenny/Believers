package com.fabsv.believers.believers.data.source.local.prefs

import android.content.Context
import com.fabsv.believers.believers.util.constants.AppConstants
import com.fabsv.believers.believers.util.prefs.SharedPreferenceManager

class AppPreferencesHelper(context: Context) {
    private var sharedPreferencesManager: SharedPreferenceManager? = null
    private var PREF_KEY_IS_LOGGED_IN: String = "PREF_KEY_IS_LOGGED_IN"
    private var PREF_KEY_IS_LOGGED_IN_USER_PHONE_NUMBER: String = "PREF_KEY_IS_LOGGED_IN_USER_PHONE_NUMBER"

    init {
        this.sharedPreferencesManager = SharedPreferenceManager(context, AppConstants.Prefs.APP_PREFERENCES)
    }

    companion object {
        private var appPreferencesHelper: AppPreferencesHelper? = null

        fun getInstance(context: Context): AppPreferencesHelper {
            if (null == appPreferencesHelper) {
                appPreferencesHelper = AppPreferencesHelper(context)
            }
            return appPreferencesHelper as AppPreferencesHelper
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferencesManager!!.getValue(PREF_KEY_IS_LOGGED_IN, false);
    }

    fun setLoggedIn(validLogin: Boolean) {
        sharedPreferencesManager!!.setValue(PREF_KEY_IS_LOGGED_IN, validLogin)
    }

    fun setLoggedInUserPhoneNumber(phoneNumberFieldValue: String) {
        sharedPreferencesManager!!.setValue(PREF_KEY_IS_LOGGED_IN_USER_PHONE_NUMBER, phoneNumberFieldValue)
    }

    fun getLoggedInUserPhoneNumber(): String {
        return sharedPreferencesManager!!.getValue(PREF_KEY_IS_LOGGED_IN_USER_PHONE_NUMBER, "")
    }
}