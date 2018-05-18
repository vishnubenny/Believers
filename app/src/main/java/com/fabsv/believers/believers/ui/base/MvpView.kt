package com.fabsv.believers.believers.ui.base

import android.support.v4.app.Fragment
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper

/**
 * Created by vishnu.benny on 2/20/2018.
 */
interface MvpView {

    fun showProgress()

    fun showShortToast(message: String)

    fun showFragment(fragment: Fragment, isAddToBackStack: Boolean)

    fun hideSoftKeyboard()

    fun getAppPreferencesHelper(): AppPreferencesHelper

    fun popBackCurrentFragment(): Boolean

    fun hasPermission(permission: String): Boolean

    fun requestPermissionsSafely(permissionArray: Array<String>, requestId: Int)

    fun safeFinishActivity()

    fun updateToolbarTitle(title: String?, homeUpEnabled: Boolean)
}