package com.fabsv.believers.believers.ui.module.login

import android.app.Activity
import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthCodeSentEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.rxVerifyPhoneNumber
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.module.home.HomeFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit

class LoginPresenter(private val context: Context, private val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<LoginContract.LoginView>(), LoginContract.LoginPresenter, AnkoLogger {

    private var compositeDisposable: CompositeDisposable
    private var loginInteractor: LoginInteractor

    //firebase phone auth
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mVerificationId: String? = null

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
//                    updateLoginOperation(isSuccessful)
                }
                .filter { isSuccessful: Boolean ->
                    isSuccessful
                }
                .switchMap { loginSuccess: Boolean ->
                    getFirebasePhoneAuthObservable()
                }
                .map { event: PhoneAuthEvent ->
                    updateLocalHolders(event as PhoneAuthCodeSentEvent)
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

    private fun firebasePhoneOtpAuth() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+919744234506", 60, TimeUnit.SECONDS, context as Activity,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential?) {

                    }

                    override fun onVerificationFailed(p0: FirebaseException?) {

                    }

                    override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                        mVerificationId = verificationId
                        mResendToken = token
                    }
                })
    }

    private fun getFirebasePhoneAuthObservable(): Observable<PhoneAuthEvent> {
        return PhoneAuthProvider.getInstance().rxVerifyPhoneNumber("+919744234506", 60, TimeUnit.SECONDS,
                context as Activity)
    }

    private fun updateLocalHolders(phoneAuthCodeSentEvent: PhoneAuthCodeSentEvent): Boolean {
        mVerificationId = phoneAuthCodeSentEvent.verificationId()
        mResendToken = phoneAuthCodeSentEvent.forceResendingToken()
        return true
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