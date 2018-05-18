package com.fabsv.believers.believers.util.prefs

import android.content.Context
import android.content.SharedPreferences


class SharedPreferenceManager {
    private var mContext: Context? = null
    private var mSettings: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    constructor(context: Context, prefFileName: String) {
        mContext = context
        mSettings = this.mContext!!.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        mEditor = this.mSettings!!.edit()
    }

    /**
     * Saves boolean value in prefs
     */
    fun setValue(key: String, booleanValue: Boolean) {
        mEditor!!.putBoolean(key, booleanValue).apply()
    }

    /**
     * Method that returns the boolean values stored in the shared preference
     */
    fun getValue(key: String, defaultValue: Boolean): Boolean {
        return mSettings!!.getBoolean(key, defaultValue)
    }

    /**
     * Save string value in the prefs
     */
    fun setValue(key: String, stringValue: String) {
        mEditor!!.putString(key, stringValue).apply()
    }

    /**
     * Method that returns the string values stored in the shared preference
     */
    fun getValue(key: String, defaultValue: String): String {
        return mSettings!!.getString(key, defaultValue)
    }

    /**
     * Save Int value in the prefs
     */
    fun setValue(key: String, intValue: Int?) {
        intValue?.let {
            mEditor?.putInt(key, it)?.apply()
        }
    }

    /**
     * Method that returns the Int values stored in the shared preference
     */
    fun getValue(key: String, defaultValue: Int): Int {
        return mSettings!!.getInt(key, defaultValue)
    }
}