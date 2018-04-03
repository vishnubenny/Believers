package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthCodeAutoRetrievalTimeOutEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthCodeSentEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthVerificationCompleteEvent
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.module.home.HomeFragment
import com.fabsv.believers.believers.util.methods.RxUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
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

        val retryButtonClickObservable: Observable<Boolean>? = getRetryButtonClickObservable()
        val retryButtonClickDisposable = retryButtonClickObservable!!.subscribe()
        this.compositeDisposable.add(retryButtonClickDisposable)

        val changeNumberClickObservable: Observable<Boolean>? = getChangeNumberClickObservable()
        val changeNumberClickDisposable = changeNumberClickObservable!!.subscribe()
        this.compositeDisposable.add(changeNumberClickDisposable)

        val verifyOtpClickObservable: Observable<Boolean>? = getVerifyOtpClickObservable()
        val verifyOtpClickDisposable = verifyOtpClickObservable!!.subscribe()
        this.compositeDisposable.add(verifyOtpClickDisposable)
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

    override fun updateOtpAttemptFailRetryLayoutStatus(isShowOtpRetryLayout: Boolean) {
        if (isViewAttached()) {
            getView()!!.updateOtpAttemptFailRetryLayoutStatus(isShowOtpRetryLayout)
        }
    }

    override fun onPhoneAuthVerificationCompleteEvent(): Boolean {
        updateLoginOperation(true)
        return true
    }

    override fun resetScreen() {
        if (isViewAttached()) {
            getView()!!.resetScreen()
        }
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
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
                }
                .filter { isSuccessful: Boolean ->
                    isSuccessful
                }
                .switchMap { loginSuccess: Boolean ->
                    getFirebaseAuthAndEventsHandling()
                }
    }

    private fun getRetryButtonClickObservable(): Observable<Boolean>? {
        return getView()!!
                .getRetryButtonClick()
                .map { event: Any -> true }
                .switchMap { retryClicked: Boolean ->
                    getFirebaseAuthAndEventsHandling()
                }
    }

    private fun getFirebaseAuthAndEventsHandling(): Observable<Boolean> {
        return loginInteractor.getFirebasePhoneAuthObservable(getView()!!.getPhoneNumberFieldValue())!!
                .map { event: PhoneAuthEvent ->
                    error(event.toString())
                    if (event is PhoneAuthVerificationCompleteEvent) {
                        onPhoneAuthVerificationCompleteEvent()
                    } else if (event is PhoneAuthCodeSentEvent) {
                        onPhoneAuthCodeSentEvent(event)
                    } else if (event is PhoneAuthCodeAutoRetrievalTimeOutEvent) {
                        onPhoneAuthCodeAutoRetrievalTimeOutEvent()
                    } else if (event is FirebaseAuthInvalidCredentialsException) {
                        return@map false
                    } else {
                        return@map false
                    }
                }
                .doOnError { error: Throwable? ->
                    error(error)
                }
                .share()
    }

    private fun getChangeNumberClickObservable(): Observable<Boolean>? {
        return getView()!!
                .getChangeNumberButtonClick()
                .map { event: Any -> true }
                .doOnNext { changeNumberClicked: Boolean ->
                    resetScreen()
                }
    }

    private fun getVerifyOtpClickObservable(): Observable<Boolean>? {
        /*return getView()!!
                .getVerifyOtpButtonClick()
                .map { event: Any -> true }
                .map { verifyOtpClicked: Boolean -> getView()!!.getVerifyOtpFieldValue() }
                .map { otpEntered: String? ->*/
//                    var credential: PhoneAuthCredential
//                    try {
//                        credential = PhoneAuthProvider.getCredential(mVerificationId!!, otpEntered!!)
//                        RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), credential)
//                    } catch (e: FirebaseException) {
//                        error(e)
//                    }
//                    Observable.error(Throwable("test"))
        /*val credential = PhoneAuthProvider.getCredential(mVerificationId!!, otpEntered!!)
        RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), credential)
    }
    .doOnError { error: Throwable? ->
        error(error)
    }
    .map { t: Single<FirebaseUser> -> t.blockingGet() }
    .map { t: FirebaseUser -> false }*/
//                .map { credential: PhoneAuthCredential ->
//                    RxFirebaseAuth.signInWithCredential(FirebaseAuth.getInstance(), credential)
//                            .toObservable()
//                }
//                .switchMap { user: Observable<FirebaseUser> -> user.isEmpty.toObservable() }
//                .doOnNext { status: Boolean? ->
//                    error("status " + status)
//                }
//                .doOnError { error: Throwable ->
//                    error(error)
//                }

        return getView()!!
                .getVerifyOtpButtonClick()
                .map { event: Any -> true }
                .map { verifyOtpClicked: Boolean -> getView()!!.getVerifyOtpFieldValue() }
                .switchMap { otpEntered: String ->
                    RxUtils.makeObservable(
                            PhoneAuthProvider.getCredential(mVerificationId!!, otpEntered)
                    )
                }
                .switchMap { credential: PhoneAuthCredential ->
                    RxUtils.makeObservable(FirebaseAuth.getInstance().signInWithCredential(credential))
                }
                .map { result: Task<AuthResult> ->
                    if (null != result.exception) {
                        return@map false
                    } else if (result.isSuccessful) {
                        return@map true
                    } else {
                        return@map false
                    }
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

    private fun updateLoginOperation(isSuccessful: Boolean) {
        info("updateLoginOperation " + isSuccessful)
        if (isViewAttached()) {
            if (isSuccessful) {
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

    private fun onPhoneAuthCodeSentEvent(phoneAuthCodeSentEvent: PhoneAuthCodeSentEvent): Boolean {
        mVerificationId = phoneAuthCodeSentEvent.verificationId()
        mResendToken = phoneAuthCodeSentEvent.forceResendingToken()
        updateVerifyAuthCodeLayoutStatus(true)
        return false
    }

    private fun onPhoneAuthCodeAutoRetrievalTimeOutEvent(): Boolean {
        updateOtpAttemptFailRetryLayoutStatus(true)
        return false
    }

    private fun updateLocalHolders(exception: FirebaseException): Boolean {
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