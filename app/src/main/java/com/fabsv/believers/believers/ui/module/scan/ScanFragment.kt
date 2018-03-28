package com.fabsv.believers.believers.ui.module.scan

import android.Manifest
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.fabsv.believers.believers.util.constants.AppConstants
import com.google.zxing.Result
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_scan.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.os.Bundle



class ScanFragment : MvpFragment<ScanContract.ScanView, ScanContract.ScanPresenter>(),
        ScanContract.ScanView, ZXingScannerView.ResultHandler {
    private var zXingScannerView: ZXingScannerView? = null

    override fun onPrepareFragment(view: View?) {
        handleCameraPermission()
        resetScanScreen()
    }

    override fun createPresenter(): ScanPresenter {
        return ScanPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_scan
    }

    override fun getScanAgainButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_scan_again)
    }

    override fun getSubmitButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_submit)
    }

    override fun getScanFieldTextChanges(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(edit_text_card_number)
    }

    override fun resetScanCameraView() {
        zXingScannerView!!.resumeCameraPreview(this)
        resetScanScreen()
    }

    override fun updateSubmitButtonState(isEnable: Boolean) {
        button_submit.isEnabled = isEnable
    }

    /**
     * Reset the scan screen : 1. UnSubscribe validations if any registered
     * 2. Reset the card number field. 3. Subscribe to the validations.
     */
    override fun resetScanScreen() {
        presenter!!.unSubscribeValidations()
        edit_text_card_number.text.clear()
        presenter!!.validate()
    }

    override fun onResume() {
        super.onResume()
        startScannerCamera()
    }

    override fun onPause() {
        super.onPause()
        stopScannerCamera()
    }

    private fun initializeScannerView() {
        zXingScannerView = ZXingScannerView(activity)
        zXingScannerView!!.setAutoFocus(true)
        frame_scanner.addView(zXingScannerView)
    }

    override fun handleResult(result: Result?) {
        if (result!!.text.isNotEmpty()) {
            edit_text_card_number.setText(result.text)
            edit_text_card_number.setSelection(edit_text_card_number.length())
        }
    }

    private fun handleCameraPermission() {
        if (hasPermission(Manifest.permission.CAMERA)) {
            initializeScannerView()
        } else {
            requestPermissionsSafely(arrayOf(Manifest.permission.CAMERA), AppConstants.PermissionConstants.REQUEST_ID_CAMERA)
        }
    }

    private fun startScannerCamera() {
        if (null != zXingScannerView) {
            zXingScannerView!!.setResultHandler(this)
            zXingScannerView!!.startCamera()
        }
    }

    private fun stopScannerCamera() {
        if (null != zXingScannerView) {
            zXingScannerView!!.stopCamera()
        }
    }

    fun onCameraPermissionGranted() {
        initializeScannerView()
        startScannerCamera()
    }

    companion object {
        fun getInstance(): Fragment {
            val fragment = ScanFragment()
            return fragment
        }
    }
}