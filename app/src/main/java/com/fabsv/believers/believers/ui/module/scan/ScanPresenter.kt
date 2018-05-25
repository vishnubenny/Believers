package com.fabsv.believers.believers.ui.module.scan

import android.app.Activity
import android.content.Context
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import com.fabsv.believers.believers.ui.module.userdetail.UserDetailFragment
import com.fabsv.believers.believers.util.constants.AppConstants
import com.fabsv.believers.believers.util.methods.UtilityMethods
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
                .map { t: Any ->
                    true
                }
                .doOnNext { clicked: Boolean? ->
                    getView()?.hideSoftKeyboard()
                }
                .flatMap { clicked: Boolean ->
                    UtilityMethods.isConnected(context as Activity)
                }
                .map { t: Boolean ->
                    getView()?.showProgress()
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
                    getView()?.hideProgress()
                    if (200 == response.code()) {
                        response.body()?.let { showUserDetailFragment(it) }
                    } else {
                        getView()?.showShortToast(context.getString(R.string.no_recodrs_found_for_the_qr_code))
                    }
                }
            }
        }, { throwable: Throwable ->
            run {
                onApiRequestException(throwable)
            }
        })
        this.compositeDisposable.add(submitButtonDisposable)
    }

    private fun onApiRequestException(throwable: Throwable) {
        getView()?.hideProgress()
        getView()?.resetScanScreen()
        if (context.getString(R.string.not_connected_to_network).equals(throwable.message, ignoreCase = true)) {
            getView()?.showShortToast(context.getString(R.string.not_connected_to_network))
        } else {
            getView()?.showShortToast(context.getString(R.string.something_went_wrong_please_contact_admin))
        }
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

    private fun clearCompositeDisposable() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    override fun detachView(retainInstance: Boolean) {
        clearCompositeDisposable()
        super.detachView(retainInstance)
    }

    init {
        this.compositeDisposable = CompositeDisposable()
        this.scanInteractor = ScanInteractor(context, appPreferencesHelper)
    }
}