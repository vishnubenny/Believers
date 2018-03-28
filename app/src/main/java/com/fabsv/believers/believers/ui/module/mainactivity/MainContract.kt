package com.fabsv.believers.believers.ui.module.mainactivity

import android.support.v4.app.Fragment
import com.lv.note.personalnote.ui.base.MvpPresenter
import com.lv.note.personalnote.ui.base.MvpView

interface MainContract {

    interface MainView : MvpView {
        fun showPermissionRequiredAlert()

    }

    interface MainPresenter : MvpPresenter<MainContract.MainView> {
        fun showFragment()
        fun onCameraPermissionGrantedForScan(currentFragmentInstance: Fragment)
        fun onPermissionDenied()

    }
}