package com.fabsv.believers.believers.data.source.local.prefs

import android.content.Context
import com.fabsv.believers.believers.data.source.remote.model.LoginResponse
import com.fabsv.believers.believers.util.constants.AppConstants
import com.fabsv.believers.believers.util.prefs.SharedPreferenceManager

class AppPreferencesHelper(context: Context) {
    private var sharedPreferencesManager: SharedPreferenceManager? = null
    private var PREF_KEY_IS_LOGGED_IN: String = "PREF_KEY_IS_LOGGED_IN"
    private var PREF_KEY_LOGGED_IN_USER_PHONE_NUMBER: String = "PREF_KEY_LOGGED_IN_USER_PHONE_NUMBER"
    private var PREF_KEY_LOGGED_IN_USER_USERNAME: String = "PREF_KEY_LOGGED_IN_USER_USERNAME"
    private var PREF_KEY_USER_ID: String = "PREF_KEY_USER_ID"
    private var PREF_KEY_MANDALAM_ID: String = "PREF_KEY_MANDALAM_ID"
    private var PREF_KEY_MAEETING_SL_NO: String = "PREF_KEY_MAEETING_SL_NO"

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

    fun setLoggedInUserUsername(username: String) {
        sharedPreferencesManager?.setValue(PREF_KEY_LOGGED_IN_USER_USERNAME, username)
    }

    fun getLoggedInUserUsername() = sharedPreferencesManager?.getValue(PREF_KEY_LOGGED_IN_USER_USERNAME, "")

    fun setLoggedInUserMobile(phoneNumberFieldValue: String) {
        sharedPreferencesManager!!.setValue(PREF_KEY_LOGGED_IN_USER_PHONE_NUMBER, phoneNumberFieldValue)
    }

    fun getLoggedInUserPhoneNumber(): String {
        return sharedPreferencesManager!!.getValue(PREF_KEY_LOGGED_IN_USER_PHONE_NUMBER, "")
    }

    private fun setUserId(userId: Int?) {
        sharedPreferencesManager?.setValue(PREF_KEY_USER_ID, userId)
    }

    private fun getUserId() = sharedPreferencesManager?.getValue(PREF_KEY_USER_ID, 0)

    private fun setMandalamId(mandalamId: Int?) {
        sharedPreferencesManager?.setValue(PREF_KEY_MANDALAM_ID, mandalamId)
    }

    private fun getMandalamId() = sharedPreferencesManager?.getValue(PREF_KEY_MANDALAM_ID, 0)

    private fun setMeetingSlNo(meetingSlNo: Int?) {
        sharedPreferencesManager?.setValue(PREF_KEY_MAEETING_SL_NO, meetingSlNo)
    }

    private fun getMeetingSlNo() = sharedPreferencesManager?.getValue(PREF_KEY_MAEETING_SL_NO, 0)

    fun setUserData(loginResponse: LoginResponse) {
        setUserId(loginResponse.userId)
        setMandalamId(loginResponse.mandalamId)
        setMeetingSlNo(loginResponse.meetingSlNo)
    }

    fun getUserData(): LoginResponse {
        val loginResponse = LoginResponse()
        loginResponse.userId = getUserId()
        loginResponse.mandalamId = getMandalamId()
        loginResponse.meetingSlNo = getMeetingSlNo()
        return loginResponse
    }
}