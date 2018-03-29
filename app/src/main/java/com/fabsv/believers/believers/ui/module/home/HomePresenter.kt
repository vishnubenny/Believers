package com.fabsv.believers.believers.ui.module.home

import android.content.Context
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.module.login.LoginFragment
import com.fabsv.believers.believers.ui.module.report.ReportFragment
import com.fabsv.believers.believers.ui.module.scan.ScanFragment
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<HomeContract.HomeView>(), HomeContract.HomePresenter {

    private var compositeDisposable: CompositeDisposable
    private var userRepository: UserRepository

    init {
        this.compositeDisposable = CompositeDisposable()
        this.userRepository = UserRepository(context, appPreferencesHelper)
    }

    override fun validate() {
        val logoutButtonObservable: Observable<Boolean> = getView()!!
                .getLogoutButtonClickEvent()
                .map { click: Any -> true }
                .switchMap { logoutClicked: Boolean ->
                    userRepository.onLogoutClicked()
                }
                .doOnNext { logoutStatus: Boolean ->
                    updateLogoutStatus(logoutStatus)
                }
        val logoutButtonDisposable = logoutButtonObservable.subscribe()
        compositeDisposable.add(logoutButtonDisposable)

        val scanButtonObservable: Observable<Boolean> = getView()!!
                .getScanButtonClickEvent()
                .map { click: Any -> true }
                .doOnNext { clicked: Boolean ->
                    showScanScreen()
                }
        val scanButtonDisposable = scanButtonObservable.subscribe()
        compositeDisposable.add(scanButtonDisposable)

        val reportButtonObservable: Observable<Boolean> = getView()!!
                .getReportButtonClickEvent()
                .map { event: Any -> true }
                .doOnNext { clicked: Boolean ->
                    showReportScreen()
                }
        val reportButtonDisposable = reportButtonObservable.subscribe()
        this.compositeDisposable.add(reportButtonDisposable)
    }

    override fun showLoggedInUserDetail() {
        if (appPreferencesHelper.getLoggedInUserPhoneNumber().isNotEmpty()) {
            showLoggedInUserPhoneNumber(appPreferencesHelper.getLoggedInUserPhoneNumber())
        }
    }

    private fun showLoggedInUserPhoneNumber(phoneNumber: String) {
        if (isViewAttached()) {
            getView()!!.showLoggedInUserPhoneNumber(phoneNumber)
        }
    }

    private fun updateLogoutStatus(logoutStatus: Boolean) {
        if (isViewAttached()) {
            if (logoutStatus) {
                getView()!!.showFragment(LoginFragment.getInstance(), false)
            } else {
                getView()!!.showShortToast(context.getString(R.string.something_went_wrong_please_try_again))
            }
        }
    }

    private fun showScanScreen() {
        if (isViewAttached()) {
            getView()!!.showFragment(ScanFragment.getInstance(), true)
        }
    }

    private fun showReportScreen() {
        if (isViewAttached()) {
            getView()!!.showFragment(ReportFragment.getInstance(), true)
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
        clearCompositeDisposable()
        super.detachView(retainInstance)
    }
}