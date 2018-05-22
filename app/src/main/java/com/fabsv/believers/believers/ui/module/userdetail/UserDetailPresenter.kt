package com.fabsv.believers.believers.ui.module.userdetail

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.text.format.Formatter
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.MakeAttendancePresentModel
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserDetailPresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) : MvpBasePresenter<UserDetailContract.UserDetailView>(), UserDetailContract.UserDetailPresenter {

    private var compositeDisposable: CompositeDisposable
    private var userDetailInteractor: UserDetailInteractor

    init {
        this.compositeDisposable = CompositeDisposable()
        this.userDetailInteractor = UserDetailInteractor(context, appPreferencesHelper)
    }

    override fun validate() {
        val rejectButtonObservable: Observable<Boolean>? = getRejectButtonObservable()
        val rejectButtonDisposable = rejectButtonObservable!!.subscribe()
        this.compositeDisposable.add(rejectButtonDisposable)

        val approveButtonObservable: Observable<Boolean>? = getApproveButtonObservable()
        val approveButtonDisposable = approveButtonObservable!!.subscribe()
        this.compositeDisposable.add(approveButtonDisposable)
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun getApproveButtonObservable(): Observable<Boolean>? {
        return getView()!!
                .getApproveButtonClickEvent()
                .map { event: Any -> true }
                .map { clicked: Boolean -> getView()?.getUserProfile() }
                .observeOn(Schedulers.io())
                .switchMap { userProfileResponse: UserProfileResponse? ->
                    when (userProfileResponse) {
                        null -> Observable.just(false)
                        else -> userDetailInteractor.makeAttendancePresent(
                                MakeAttendancePresentModel.create(userProfileResponse, appPreferencesHelper, getIp()))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { status: Boolean ->
                    if (status) {
                        approveStatusUpdateSuccess()
                    } else {
                        approveStatusUpdateFailed()
                    }
                }
    }

    private fun getIp(): String {
        val wifiManager = context.getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        return ip
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
            getView()?.onApproveStatusUpdateSuccess()
        }
    }

    private fun approveStatusUpdateFailed() {
        if (isViewAttached()) {
            getView()?.onApproveStatusUpdateFailed()
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