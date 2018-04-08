package com.fabsv.believers.believers.ui.module.mainactivity

import android.content.Context
import android.support.v4.app.Fragment
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.module.home.HomeFragment
import com.fabsv.believers.believers.ui.module.login.LoginFragment
import com.fabsv.believers.believers.ui.module.scan.ScanFragment
import com.lv.note.personalnote.ui.base.MvpBasePresenter

class MainPresenter(private val context: Context, private val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<MainContract.MainView>(), MainContract.MainPresenter {
    override fun showFragment() {
        if (appPreferencesHelper.isLoggedIn()) {
            showFragment(HomeFragment.getInstance(), false)
        } else {
            showFragment(LoginFragment.getInstance(), false)
        }
    }

    override fun onCameraPermissionGrantedForScan(currentFragmentInstance: Fragment) {
        if (isViewAttached()) {
            if (currentFragmentInstance is ScanFragment) {
                currentFragmentInstance.onCameraPermissionGranted()
            }
        }
    }

    override fun onPermissionDenied() {
        if (isViewAttached()) {
            getView()!!.showPermissionRequiredAlert()
        }
    }

    fun showFragment(fragment: Fragment, isAddedToBackStack: Boolean) {
        if (isViewAttached()) {
            getView()!!.showFragment(fragment, isAddedToBackStack)
        }
    }

}