package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthCodeSentEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthVerificationCompleteEvent
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.module.home.HomeFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class LoginPresenter(private val context: Context, private val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<LoginContract.LoginView>(), LoginContract.LoginPresenter, AnkoLogger {

    private var compositeDisposable: CompositeDisposable
    private var loginInteractor: LoginInteractor

    //firebase phone auth
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mVerificationId: String? = null

    override fun validate() {
        val phoneNumberObservable: Observable<Boolean>? = getPhoneNumberObservable()
        val phoneNumberDisposable = phoneNumberObservable!!.subscribe()
        this.compositeDisposable.add(phoneNumberDisposable)

        val loginOperationObservable: Observable<Boolean>? = getLoginOperationObservable()
        val loginOperationDisposable = loginOperationObservable!!.subscribe()
        this.compositeDisposable.add(loginOperationDisposable)

        val verifyOtpObservable: Observable<Boolean>? = getVerifyOtpObservable()
        val verifyOtpDisposable = verifyOtpObservable!!.subscribe()
        this.compositeDisposable.add(verifyOtpDisposable)

        val verifyOtpOperationObservable: Observable<Boolean>? = getVerifyOtpOperationObservable()
        val verifyOtpOperationDisposable = verifyOtpOperationObservable!!.subscribe()
        this.compositeDisposable.add(verifyOtpOperationDisposable)
    }

    override fun showHomeFragment() {
        if (isViewAttached()) {
            getView()!!.showFragment(HomeFragment.getInstance(), false)
        }
    }

    override fun updateVerifyAuthCodeLayoutStatus(showVerifyLayout: Boolean) {
        if (isViewAttached()) {
            getView()!!.updateVerifyAuthCodeLayoutStatus(showVerifyLayout)
        }
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun getVerifyOtpOperationObservable(): Observable<Boolean>? {
        return getView()!!
                .getVerifyOtpButtonClick()
                .map { event: Any -> true }
                .map { clicked: Boolean -> getView()!!.getOtpFieldValue() }
                .switchMap { otpEntered: String ->
                    loginInteractor.firebaseVerifyOtp(mVerificationId!!, otpEntered)!!.toObservable()
                }
                .map { user: FirebaseUser -> true }
                .doOnNext { isLoginSucceeded: Boolean ->
                    updateLoginOperation(isLoginSucceeded)
                }
                .doOnError {
                    updateLoginOperation(false)
                    error(it)
                }
    }

    private fun getLoginOperationObservable(): Observable<Boolean>? {
        return getView()!!
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
                    //                    updateLoginOperation(isSuccessful)
                }
                .filter { isSuccessful: Boolean ->
                    isSuccessful
                }
                .switchMap { loginSuccess: Boolean ->
                    loginInteractor.getFirebasePhoneAuthObservable()
                }
                .map { event: PhoneAuthEvent ->
                    error(event.toString())
                    if (event is PhoneAuthVerificationCompleteEvent) {
                        updateLocalHolders(event)
                    } else if (event is PhoneAuthCodeSentEvent){
                        updateLocalHolders(event)
                    } else {
                        updateLocalHolders(event as FirebaseAuthInvalidCredentialsException)
                    }
                }
                .doOnNext { showVerify: Boolean ->
                    updateVerifyAuthCodeLayoutStatus(showVerify)
                }
    }

    private fun getPhoneNumberObservable(): Observable<Boolean>? {
        return getView()!!
                .getPhoneNumberField()
                .map { it: CharSequence ->
                    it.toString().length == 10
                }
                .doOnNext { isValid: Boolean ->
                    updateLoginButtonStatus(isValid)
                }
    }

    private fun getVerifyOtpObservable(): Observable<Boolean>? {
        return getView()!!
                .getOtpField()
                .map { fieldValue: CharSequence -> fieldValue.length == 6 }
                .doOnNext { isValidOtp: Boolean ->
                    updateVerifyOtpButtonStatus(isValidOtp)
                }
    }

    private fun updateVerifyOtpButtonStatus(isValidOtp: Boolean) {
        if (isViewAttached()) {
            getView()!!.updateVerifyOtpButtonStatus(isValidOtp)
        }
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

    private fun updateLocalHolders(phoneAuthCodeSentEvent: PhoneAuthCodeSentEvent): Boolean {
        mVerificationId = phoneAuthCodeSentEvent.verificationId()
        mResendToken = phoneAuthCodeSentEvent.forceResendingToken()
        return true
    }

    private fun updateLocalHolders(phoneAuthVerificationCompleteEvent: PhoneAuthVerificationCompleteEvent) : Boolean {
        error(phoneAuthVerificationCompleteEvent.credential().toString())
        return true
    }

    private fun updateLocalHolders(exception: FirebaseAuthInvalidCredentialsException): Boolean {
        error(exception.toString())
        return false
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