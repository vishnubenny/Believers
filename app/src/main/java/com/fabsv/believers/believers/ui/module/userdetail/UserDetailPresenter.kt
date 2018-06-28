package com.fabsv.believers.believers.ui.module.userdetail

import android.app.Activity
import android.content.Context
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.MakeAttendancePresentModel
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import com.fabsv.believers.believers.util.methods.UtilityMethods
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class UserDetailPresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<UserDetailContract.UserDetailView>(), UserDetailContract.UserDetailPresenter, AnkoLogger {

    private var compositeDisposable: CompositeDisposable
    private var userDetailInteractor: UserDetailInteractor

    init {
        this.compositeDisposable = CompositeDisposable()
        this.userDetailInteractor = UserDetailInteractor(context, appPreferencesHelper)
    }

    override fun validate() {
        rejectButtonObservableHandler()

        approveButtonEnableObservableHandler()

        approveButtonObservableHandler()
    }

    private fun rejectButtonObservableHandler() {
        val rejectButtonObservable: Observable<Boolean>? = getRejectButtonObservable()
        val rejectButtonDisposable = rejectButtonObservable?.subscribe()
        rejectButtonDisposable?.let { this.compositeDisposable.add(it) }
    }

    private fun approveButtonEnableObservableHandler() {
        val approveButtonEnableObservable: Observable<Boolean>? = getApproveButtonEnableObservable()
        val approveButtonEnableDisposable = approveButtonEnableObservable?.subscribe()
        approveButtonEnableDisposable?.let { this.compositeDisposable.add(it) }
    }

    private fun approveButtonObservableHandler() {
        val approveButtonObservable: Observable<Boolean>? = getApproveButtonObservable()
        val approveButtonDisposable = approveButtonObservable?.subscribe(
                {
                    getView()?.hideProgress()
                    if (it) {
                        approveStatusUpdateSuccess()
                    } else {
                        approveStatusUpdateFailed()
                    }
                },
                {
                    onApiException(it)
                }
        )
        approveButtonDisposable?.let { this.compositeDisposable.add(it) }
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun getApproveButtonObservable(): Observable<Boolean>? {
        return getView()!!
                .getApproveButtonClickEvent()
                .map { event: Any ->
                    getView()?.hideSoftKeyboard()
                    true
                }
                .flatMap { clicked: Boolean ->
                    UtilityMethods.isConnected(context as Activity)
                }
                .map { clicked: Boolean ->
                    getView()?.showProgress()
                    getView()?.getUserProfile()
                }
                .observeOn(Schedulers.io())
                .switchMap { userProfileResponse: UserProfileResponse? ->
                    when (userProfileResponse) {
                        null -> Observable.just(false)
                        else -> userDetailInteractor.makeAttendancePresent(
                                MakeAttendancePresentModel.create(userProfileResponse, appPreferencesHelper, getIp()))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getApproveButtonEnableObservable() = getView()?.getUserProfileValidity()
            ?.map { isValid: Boolean -> !isValid }
            ?.map { enable: Boolean ->
                getView()?.updateApproveButtonEnableStatus(enable)
                enable
            }

    private fun getIp(): String {
        /*val wifiManager = context.getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        return ip*/
        /*
        Pass phone number instead of ip in the same key in api for approval. Update as of 26/06/2018
         */
        return appPreferencesHelper.getLoggedInUserPhoneNumber()
    }

    private fun getRejectButtonObservable(): Observable<Boolean>? {
        return getView()!!
                .getRejectButtonClickEvent()
                .map { event: Any -> true }
                .doOnNext { exitUserDetailScreen() }
    }

    private fun exitUserDetailScreen() {
        if (isViewAttached()) {
            getView()?.exitUserDetailScreen()
        }
    }

    private fun approveStatusUpdateSuccess() {
        if (isViewAttached()) {
            getView()?.resetScreen()
            getView()?.onApproveStatusUpdateSuccess()
        }
    }

    private fun approveStatusUpdateFailed() {
        if (isViewAttached()) {
            getView()?.resetScreen()
            getView()?.onApproveStatusUpdateFailed()
        }
    }

    private fun onApiException(throwable: Throwable) {
        error("test " + throwable)
        getView()?.hideProgress()
        getView()?.resetScreen()
        if (context.getString(R.string.not_connected_to_network).equals(throwable.message, ignoreCase = true)) {
            getView()?.showShortToast(context.getString(R.string.not_connected_to_network))
        } else {
            getView()?.showShortToast(context.getString(R.string.something_went_wrong_please_contact_admin))
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
}