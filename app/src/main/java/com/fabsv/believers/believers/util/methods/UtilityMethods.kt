package com.fabsv.believers.believers.util.methods

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import com.fabsv.believers.believers.R
import io.reactivex.Observable

class UtilityMethods {
    companion object {
        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            view?.let {
                val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }

        fun isConnected(activity: Activity): Observable<Boolean> {
            val connectivityManager: ConnectivityManager =
                    activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (null != connectivityManager.activeNetworkInfo) {
                return RxUtils.makeObservable(true)
            } else {
                val throwable = Throwable(activity.getString(R.string.not_connected_to_network))
                return Observable.error<Boolean>(throwable)
            }
        }
    }
}