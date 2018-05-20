package com.fabsv.believers.believers.ui.module.userdetail

import android.content.Context
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.MakeAttendancePresentModel
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

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
                .switchMap { userProfileResponse: UserProfileResponse? ->
                    when (userProfileResponse) {
                        null -> Observable.just(false)
                        else -> userDetailInteractor.makeAttendancePresent(MakeAttendancePresentModel.create(userProfileResponse, appPreferencesHelper))
                    }
                }
                .doOnNext { status: Boolean ->
                    if (status) {
                        approveStatusUpdateSuccess()
                    } else {
                        apporveStatusUpdateFailed()
                    }
                }
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

    private fun apporveStatusUpdateFailed() {
        if (isViewAttached()) {
            getView()?.onApproveStatusUpdateFailed()
        }
    }

    private fun clearCompositeDisposable() {
        if (null != compositeDisposable && !compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    private fun disposeCompositeDisposable() {
        if (null != compositeDisposable && !compositeDisposable.isDisposed) {
            compositeDisposable.clear()
            compositeDisposable.dispose()
        }
    }

    override fun detachView(retainInstance: Boolean) {
        disposeCompositeDisposable()
        super.detachView(retainInstance)
    }
}