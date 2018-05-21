package com.fabsv.believers.believers.ui.module.scan

import android.content.Context
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import com.fabsv.believers.believers.ui.module.userdetail.UserDetailFragment
import com.fabsv.believers.believers.util.constants.AppConstants
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ScanPresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<ScanContract.ScanView>(), ScanContract.ScanPresenter {

    private var compositeDisposable: CompositeDisposable
    private var scanInteractor: ScanInteractor

    override fun validate() {
        scanFieldValueChangeObservableHandler()

        scanAgainButtonObservableHandler()

        submitButtonObservableHandler()
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun scanFieldValueChangeObservableHandler() {
        val scanValueObservable: Observable<Boolean> = getView()!!
                .getScanFieldTextChanges()
                .map { cardNumber: CharSequence -> cardNumber.length >= AppConstants.CardDataConstants.CARD_NUMBER_MINIMUM_LENGTH }
                .doOnNext { isEnable: Boolean ->
                    updateSubmitButtonState(isEnable)
                }
        val scanValueDisposable = scanValueObservable.subscribe()
        this.compositeDisposable.add(scanValueDisposable)
    }

    private fun submitButtonObservableHandler() {
        val submitButtonObservable: Observable<Response<UserProfileResponse>> = getView()!!
                .getSubmitButtonClickEvent()
                .map { t: Any -> true }
                .doOnNext { t: Boolean? -> true }
                .filter { t: Boolean -> t }
                .map { t: Boolean ->
                    getView()?.hideSoftKeyboard()
                    getView()?.getQrCodeFieldValue()
                }
                .observeOn(Schedulers.io())
                .switchMap { qrCode: String ->
                    scanInteractor.requestQrCodeData(qrCode)
                }
                .observeOn(AndroidSchedulers.mainThread())
        val submitButtonDisposable = submitButtonObservable.subscribe({ response: Response<UserProfileResponse>? ->
            run {
                response?.let {
                    if (200 == response.code()) {
                        response.body()?.let { showUserDetailFragment(it) }
                    } else {
                        getView()?.showShortToast(context.getString(R.string.no_recodrs_found_for_the_qr_code))
                    }
                }
            }
        }, { throwable: Throwable ->
            run {
                getView()?.showShortToast(context.getString(R.string.something_went_wrong_please_contact_admin))
            }
        })
        this.compositeDisposable.add(submitButtonDisposable)
    }

    private fun scanAgainButtonObservableHandler() {
        val scanAgainButtonObservable: Observable<Boolean> = getView()!!
                .getScanAgainButtonClickEvent()
                .map { event: Any -> true }
                .doOnNext { clicked: Boolean ->
                    resetScanCameraView()
                }
        val scanAgainButtonDisposable = scanAgainButtonObservable.subscribe()
        this.compositeDisposable.add(scanAgainButtonDisposable)
    }

    private fun resetScanCameraView() {
        if (isViewAttached()) {
            getView()?.resetScanCameraView()
        }
    }

    private fun showUserDetailFragment(userProfileResponse: UserProfileResponse) {
        if (isViewAttached()) {
            getView()?.resetScanScreen()
            getView()?.showFragment(UserDetailFragment.getInstance(userProfileResponse), true)
        }
    }

    private fun updateSubmitButtonState(isEnable: Boolean) {
        if (isViewAttached()) {
            getView()!!.updateSubmitButtonState(isEnable)
        }
    }

    private fun disposeCompositeDisposable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
            compositeDisposable.dispose()
        }
    }

    private fun clearCompositeDisposable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    override fun detachView(retainInstance: Boolean) {
        disposeCompositeDisposable()
        super.detachView(retainInstance)
    }

    init {
        this.compositeDisposable = CompositeDisposable()
        this.scanInteractor = ScanInteractor(context, appPreferencesHelper)
    }
}