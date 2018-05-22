package com.fabsv.believers.believers.ui.module.home

import android.content.Context
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.CollectionReportResponse
import com.fabsv.believers.believers.data.source.remote.model.QuorumReportResponse
import com.fabsv.believers.believers.ui.module.login.LoginFragment
import com.fabsv.believers.believers.ui.module.report.ReportFragment
import com.fabsv.believers.believers.ui.module.scan.ScanFragment
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger

class HomePresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<HomeContract.HomeView>(), HomeContract.HomePresenter, AnkoLogger {

    private var compositeDisposable: CompositeDisposable
    private var userRepository: UserRepository
    private var homeInteractor = HomeInteractor(context, appPreferencesHelper)

    init {
        this.compositeDisposable = CompositeDisposable()
        this.userRepository = UserRepository(context, appPreferencesHelper)
    }

    override fun validate() {
        logoutButtonObservableHandler()

        scanButtonObservableHandler()

        collectionReportButtonObservableHandler()

        quorumReportButtonObservableHandler()
    }

    override fun showLoggedInUserDetail() {
        if (appPreferencesHelper.getLoggedInUserPhoneNumber().isNotEmpty()) {
            showLoggedInUserPhoneNumber(appPreferencesHelper.getLoggedInUserPhoneNumber())
        }
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun scanButtonObservableHandler() {
        val scanButtonObservable: Observable<Boolean> = getView()!!
                .getScanButtonClickEvent()
                .map { click: Any -> true }
                .doOnNext { clicked: Boolean ->
                    showScanScreen()
                }

        val scanButtonDisposable = scanButtonObservable.subscribe()
        compositeDisposable.add(scanButtonDisposable)
    }

    private fun logoutButtonObservableHandler() {
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
    }

    private fun collectionReportButtonObservableHandler() {
        getView()?.let {
            compositeDisposable.add(it.getCollectionReportButtonClickEvent()
                    .map {
                        getView()?.showProgress()
                    }
                    .doOnNext {
                        compositeDisposable.add(homeInteractor.getCollectionReport()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        {
                                            getView()?.hideProgress()
                                            if (it.isSuccessful()) {
                                                it.data?.let {
                                                    showCollectionReportScreen(it)
                                                }
                                            } else {
                                                getView()?.showShortToast(context.getString(R.string.report_fetch_failed))
                                            }
                                        },
                                        {
                                            getView()?.hideProgress()
                                            getView()?.showShortToast(context.getString(R.string.something_went_wrong_please_contact_admin))
                                        }
                                ))
                    }
                    .subscribe())
        }
    }

    private fun quorumReportButtonObservableHandler() {
        getView()?.let {
            compositeDisposable.add(it.getQuorumReportButtonClickEvent()
                    .map {
                        getView()?.showProgress()
                    }
                    .doOnNext {
                        compositeDisposable.add(homeInteractor.getQuorumReport()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        {
                                            getView()?.hideProgress()
                                            if (it.isSuccessful()) {
                                                it.data?.let {
                                                    showCollectionReportScreen(it)
                                                }
                                            } else {
                                                getView()?.showShortToast(context.getString(R.string.report_fetch_failed))
                                            }
                                        },
                                        {
                                            getView()?.hideProgress()
                                            getView()?.showShortToast(context.getString(R.string.something_went_wrong_please_contact_admin))
                                        }

                                ))
                    }
                    .subscribe())
        }
    }

    private fun showLoggedInUserPhoneNumber(phoneNumber: String) {
        if (isViewAttached()) {
            getView()?.showLoggedInUserPhoneNumber(phoneNumber)
        }
    }

    private fun updateLogoutStatus(logoutStatus: Boolean) {
        if (isViewAttached()) {
            if (logoutStatus) {
                getView()?.showFragment(LoginFragment.getInstance(), false)
            } else {
                getView()?.showShortToast(context.getString(R.string.something_went_wrong_please_contact_admin))
            }
        }
    }

    private fun showScanScreen() {
        if (isViewAttached()) {
            getView()?.showFragment(ScanFragment.getInstance(), true)
        }
    }

    private fun showCollectionReportScreen(collectionReportResponse: Any) {
        if (isViewAttached()) {
            getView()?.showFragment(ReportFragment.getInstance(collectionReportResponse), true)
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