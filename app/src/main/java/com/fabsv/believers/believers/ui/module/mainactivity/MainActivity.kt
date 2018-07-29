package com.fabsv.believers.believers.ui.module.mainactivity

import android.Manifest
import android.content.pm.PackageManager
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.ui.base.MvpActivity
import com.fabsv.believers.believers.ui.utils.AppAlertDialog
import com.fabsv.believers.believers.util.constants.AppConstants

class MainActivity : MvpActivity<MainContract.MainView, MainContract.MainPresenter>(),
        MainContract.MainView, AppAlertDialog.OnAlertButtonClick {
    override fun onPrepareActivity() {
        handleCameraPermission()

        presenter?.showFragment();
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun showPermissionRequiredAlert() {
        val appAlertDialog = AppAlertDialog(this, getString(R.string.app_will_not_work_without_camera_permission),
                "CONTINUE", "EXIT", this)
        val alert = appAlertDialog.getAlertDialog(false)
        alert.show()
    }

    override fun onPositiveButtonClick() {
        requestPermissionsSafely(arrayOf(Manifest.permission.CAMERA), AppConstants.PermissionConstants.REQUEST_ID_CAMERA)
    }

    override fun onNegativeButtonClick() {
        safeFinishActivity()
    }

    private fun handleCameraPermission() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            requestPermissionsSafely(arrayOf(Manifest.permission.CAMERA), AppConstants.PermissionConstants.REQUEST_ID_CAMERA)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (AppConstants.PermissionConstants.REQUEST_ID_CAMERA == requestCode) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                getCurrentFragmentInstance()?.let { presenter?.onCameraPermissionGrantedForScan(it) }
            } else {
                presenter?.onPermissionDenied()
            }
        }
    }
}
