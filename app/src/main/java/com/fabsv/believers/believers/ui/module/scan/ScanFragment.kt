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
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_scan.*
import me.dm7.barcodescanner.zxing.ZXingScannerView


class ScanFragment : MvpFragment<ScanContract.ScanView, ScanContract.ScanPresenter>(),
        ScanContract.ScanView, ZXingScannerView.ResultHandler {
    private var zXingScannerView: ZXingScannerView? = null
    private val scanButtonClickPublishSubject = PublishSubject.create<Boolean>()
    private val isScanned = PublishSubject.create<Boolean>()

    override fun onPrepareFragment(view: View?) {
        handleCameraPermission()
        presetScreen()
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

    override fun getSubmitButtonClickEvent(): Observable<Boolean> {
        return scanButtonClickPublishSubject
    }

    override fun getScanFieldTextChanges(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(edit_text_qr_code)
    }

    override fun resetScanCameraView() {
        resetScanScreen()
    }

    override fun updateSubmitButtonState(isEnable: Boolean) {
        button_submit.isEnabled = isEnable
    }

    override fun getQrCodeFieldValue(): String {
        return edit_text_qr_code.text.toString()
    }

    override fun getScannedOperationListener(): Observable<Boolean> = isScanned

    /**
     * Reset the scan screen : 1. UnSubscribe validations if any registered
     * 2. Reset the card number field. 3. Subscribe to the validations.
     */
    override fun resetScanScreen() {
        presenter?.unSubscribeValidations()
        hideSoftKeyboard()
        presetFields()
        presenter?.validate()
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
        zXingScannerView?.setAutoFocus(true)
        frame_scanner.addView(zXingScannerView)
    }

    override fun handleResult(result: Result?) {
        result?.let {
            edit_text_qr_code.setText(it.text)
            edit_text_qr_code.setSelection(edit_text_qr_code.length())
            isScanned.onNext(true)
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
        zXingScannerView?.let {
            it.setResultHandler(this)
            it.startCamera()
        }
    }

    private fun stopScannerCamera() {
        zXingScannerView?.stopCamera()
    }

    private fun presetScreen() {
        updateToolbarTitle(activity?.getString(R.string.scan_qr), homeUpEnabled = true)
        button_submit.setOnClickListener {
            scanButtonClickPublishSubject.onNext(true)
        }
    }

    private fun presetFields() {
        edit_text_qr_code.text.clear()
        edit_text_qr_code.setText("")
        edit_text_qr_code.requestFocus()
        zXingScannerView?.resumeCameraPreview(this)
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