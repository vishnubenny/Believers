package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.module.home.HomeFragment
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class LoginPresenter(private val context: Context, private val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<LoginContract.LoginView>(), LoginContract.LoginPresenter, AnkoLogger {

    private var compositeDisposable: CompositeDisposable
    private var loginInteractor: LoginInteractor

    override fun validate() {
        val phoneNumberObservable: Observable<Boolean> = getView()!!
                .getPhoneNumberField()
                .map { it: CharSequence ->
                    it.toString().length == 10
                }
                .doOnNext { isValid: Boolean ->
                    updateLoginButtonStatus(isValid)
                }
        val phoneNumberDisposable = phoneNumberObservable.subscribe()
        compositeDisposable.add(phoneNumberDisposable)

        val loginOperationObservable: Observable<Boolean> = getView()!!
                .getLoginButtonClick()
                .map { t: Any -> true }
                .map { clicked: Boolean -> getView()!!.getPhoneNumberFieldValue() }
                .observeOn(Schedulers.io())
                .switchMap { phoneNumber: String ->
                    loginInteractor.loginWithPhoneNumber(phoneNumber)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isSuccessful: Boolean ->
                    if (isSuccessful) {
                        updateUserPrefs(isSuccessful, getView()!!.getPhoneNumberFieldValue())
                    }
                    updateLoginOperation(isSuccessful)
                }
        val loginOperationDisposable = loginOperationObservable.subscribe()
        compositeDisposable.add(loginOperationDisposable)
    }

    override fun showHomeFragment() {
        if (isViewAttached()) {
            getView()!!.showFragment(HomeFragment.getInstance(), false)
        }
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun updateLoginOperation(successful: Boolean) {
        info("updateLoginOperation " + successful)
        if (isViewAttached()) {
            if (successful) {
                getView()!!.onLoginSuccess()
                getView()!!.resetScreen()
            } else {
                getView()!!.onLoginFailure()
            }
        }
    }

    private fun updateUserPrefs(validLogin: Boolean, phoneNumberFieldValue: String) {
        appPreferencesHelper.setLoggedIn(validLogin)
        appPreferencesHelper.setLoggedInUserPhoneNumber(phoneNumberFieldValue)
    }

    private fun updateLoginButtonStatus(isEnable: Boolean) {
        if (isViewAttached()) {
            getView()!!.updateLoginButtonStatus(isEnable)
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

    init {
        this.compositeDisposable = CompositeDisposable()
        this.loginInteractor = LoginInteractor(context, appPreferencesHelper)
    }

    override fun detachView(retainInstance: Boolean) {
        disposeCompositeDisposable()
        super.detachView(retainInstance)
    }
}