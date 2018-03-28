package com.fabsv.believers.believers.ui.module.mainactivity

import android.content.pm.PackageManager
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.ui.base.MvpActivity
import com.fabsv.believers.believers.ui.utils.AppAlertDialog
import com.fabsv.believers.believers.util.constants.AppConstants

class MainActivity : MvpActivity<MainContract.MainView, MainContract.MainPresenter>(),
        MainContract.MainView {
    override fun onPrepareActivity() {

        presenter!!.showFragment();
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun showPermissionRequiredAlert() {
        val appAlertDialog = AppAlertDialog(this, getString(R.string.app_will_not_work_without_camera_permission),
                "EXIT", "CONTINUE", this, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (AppConstants.PermissionConstants.REQUEST_ID_CAMERA == requestCode) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                presenter!!.onCameraPermissionGrantedForScan(getCurrentFragmentInstance())
            } else {
                presenter!!.onPermissionDenied()
            }
        }
    }
}
