package com.fabsv.believers.believers.util.methods

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class utilityMethods {
    companion object {
        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            view?.let {
                val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}