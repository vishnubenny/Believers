package com.fabsv.believers.believers.ui.module.scan

import android.content.Context
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.module.userdetail.UserDetailFragment
import com.fabsv.believers.believers.util.constants.AppConstants
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class ScanPresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<ScanContract.ScanView>(), ScanContract.ScanPresenter {

    private var compositeDisposable: CompositeDisposable

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    override fun validate() {
        val scanValueObservable: Observable<Boolean> = getView()!!
                .getScanFieldTextChanges()
                .map { cardNumber: CharSequence -> cardNumber.length >= AppConstants.CardDataConstants.CARD_NUMBER_MINIMUM_LENGTH }
                .doOnNext { isEnable: Boolean ->
                    updateSubmitButtonState(isEnable)
                }
        val scanValueDisposable = scanValueObservable.subscribe()
        this.compositeDisposable.add(scanValueDisposable)

        val scanAgainButtonObservable: Observable<Boolean> = getView()!!
                .getScanAgainButtonClickEvent()
                .map { event: Any -> true }
                .doOnNext { clicked: Boolean ->
                    resetScanCameraView()
                }
        val scanAgainButtonDisposable = scanAgainButtonObservable.subscribe()
        this.compositeDisposable.add(scanAgainButtonDisposable)

        val submitButtonObservable: Observable<Boolean> = getView()!!
                .getSubmitButtonClickEvent()
                .map { event: Any -> true }
                .doOnNext { clicked: Boolean ->
                    showUserDetailFragment()
                }
        val submitButtonDisposable = submitButtonObservable.subscribe()
        this.compositeDisposable.add(submitButtonDisposable)
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun resetScanCameraView() {
        if (isViewAttached()) {
            getView()!!.resetScanCameraView()
        }
    }

    private fun showUserDetailFragment() {
        if (isViewAttached()) {
            getView()!!.resetScanScreen()
            getView()!!.showFragment(UserDetailFragment.getInstance(), true)
        }
    }

    private fun updateSubmitButtonState(isEnable: Boolean) {
        if (isViewAttached()) {
            getView()!!.updateSubmitButtonState(isEnable)
        }
    }

    private fun clearCompositeDisposable() {
        if (null != compositeDisposable && !compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    override fun detachView(retainInstance: Boolean) {
        clearCompositeDisposable()
        super.detachView(retainInstance)
    }
}