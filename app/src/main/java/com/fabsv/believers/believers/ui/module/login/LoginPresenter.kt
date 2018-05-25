package com.fabsv.believers.believers.ui.module.login

import android.app.Activity
import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthCodeAutoRetrievalTimeOutEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthCodeSentEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.PhoneAuthVerificationCompleteEvent
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.LoginRequest
import com.fabsv.believers.believers.ui.module.home.HomeFragment
import com.fabsv.believers.believers.util.methods.RxUtils
import com.fabsv.believers.believers.util.methods.UtilityMethods
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class LoginPresenter(private val context: Context, private val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<LoginContract.LoginView>(), LoginContract.LoginPresenter, AnkoLogger {

    private var compositeDisposable: CompositeDisposable
    private var loginInteractor: LoginInteractor

    //firebase phone auth
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mVerificationId: String? = null

    override fun validate() {
        val usernameObservable: Observable<Boolean>? = getUsernameObservable()
        val passwordObservable: Observable<Boolean>? = getPasswordObservable()
        val phoneNumberObservable: Observable<Boolean>? = getPhoneNumberObservable()

        val combinedObservable = Observable.combineLatest(usernameObservable, passwordObservable, phoneNumberObservable,
                Function3 { usernameStatus: Boolean, passwordStatus: Boolean, mobileStatus: Boolean -> usernameStatus && passwordStatus && mobileStatus })
        val combinedDisposable = combinedObservable?.doOnNext { status: Boolean? -> status?.let { updateLoginButtonStatus(it) } }?.subscribe()
        combinedDisposable?.let { this.compositeDisposable.add(it) }

        loginButtonObservableHandler()

        val retryButtonClickObservable: Observable<Boolean>? = getRetryButtonClickObservable()
        val retryButtonClickDisposable = retryButtonClickObservable!!.subscribe()
        this.compositeDisposable.add(retryButtonClickDisposable)

        val changeNumberClickObservable: Observable<Boolean>? = getChangeNumberClickObservable()
        val changeNumberClickDisposable = changeNumberClickObservable!!.subscribe()
        this.compositeDisposable.add(changeNumberClickDisposable)

        val otpNumberObservable: Observable<Boolean>? = getOtpNumberObservable()
        val otpNumberDisposable = otpNumberObservable!!.subscribe()
        this.compositeDisposable.add(otpNumberDisposable)


        val verifyOtpClickObservable: Observable<Boolean>? = getVerifyOtpClickObservable()
        val verifyOtpClickDisposable = verifyOtpClickObservable!!.subscribe()
        this.compositeDisposable.add(verifyOtpClickDisposable)
    }

    private fun loginButtonObservableHandler() {
        val loginOperationObservable: Observable<Boolean>? = getLoginOperationObservable()
        val loginOperationDisposable = loginOperationObservable?.subscribe(
                { isSuccessful: Boolean ->
                    if (isSuccessful) {
                        getView()?.getLoginRequestModel()?.username?.let { appPreferencesHelper.setLoggedInUserUsername(it) }
                        getView()?.getLoginRequestModel()?.mobileNumber?.let { updateUserPrefs(isSuccessful, it) }
                    }
                    updateLoginOperation(isSuccessful)
                },
                {
                    onApiException(it)
                }
        )
        loginOperationDisposable?.let { this.compositeDisposable.add(it) }
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
            getView()?.resetScreen()
        }
    }

    override fun unSubscribeValidations() {
        clearCompositeDisposable()
    }

    private fun getLoginOperationObservable(): Observable<Boolean>? {
        return getView()!!
                .getLoginButtonClick()
                .map { t: Any ->
                    getView()?.hideSoftKeyboard()
                    true
                }
                .flatMap { clicked: Boolean -> UtilityMethods.isConnected(context as Activity) }
                .map { isConnected: Boolean ->
                    getView()?.showProgress()
                    return@map getView()?.getLoginRequestModel()
                }
                .observeOn(Schedulers.io())
                .switchMap { loginRequest: LoginRequest ->
                    loginInteractor.loginWithPhoneNumber(loginRequest)
                }
                .observeOn(AndroidSchedulers.mainThread())
        /*.filter { isSuccessful: Boolean ->
            isSuccessful
        }
        .switchMap { loginSuccess: Boolean ->
            getFirebaseAuthAndEventsHandling()
        }*/
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
        return getView()?.getLoginRequestModel()?.mobileNumber?.let { loginInteractor.getFirebasePhoneAuthObservable(it) }!!
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

    private fun getOtpNumberObservable(): Observable<Boolean>? {
        return getView()!!
                .getOtpNumberObservable()
                .map { otpEntered: CharSequence -> otpEntered.length == 6 }
                .doOnNext { isValidOtpFormat: Boolean ->
                    updateVerifyOtpButtonStatus(isValidOtpFormat)
                }
    }

    private fun getVerifyOtpClickObservable(): Observable<Boolean>? {
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
                    RxUtils.makeObservable(FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
                        updateLoginOperation(task.isSuccessful)
                    })
                }
                .map { t: Task<AuthResult> -> true }
    }

    private fun getUsernameObservable() =
            getView()?.getUsernameField()?.map { t: CharSequence -> t.isNotEmpty() }

    private fun getPasswordObservable(): Observable<Boolean>? =
            getView()?.getPasswordField()?.map { t: CharSequence -> t.isNotEmpty() }

    private fun getPhoneNumberObservable() =
            getView()?.getPhoneNumberField()?.map { it: CharSequence -> it.toString().length == 10 }

    private fun updateLoginOperation(isSuccessful: Boolean) {
        if (isViewAttached()) {
            getView()?.hideProgress()
            if (isSuccessful) {
                getView()?.onLoginSuccess()
                getView()?.resetScreen()
            } else {
                getView()?.onLoginFailure()
            }
        }
    }

    private fun onApiException(throwable: Throwable) {
        if (isViewAttached()) {
            getView()?.hideProgress()
            getView()?.resetScreen()
            if (context.getString(R.string.not_connected_to_network).equals(throwable.message, ignoreCase = true)) {
                getView()?.showShortToast(context.getString(R.string.not_connected_to_network))
            } else {
                getView()?.showShortToast(context.getString(R.string.something_went_wrong_please_contact_admin))
            }
        }
    }

    private fun updateUserPrefs(validLogin: Boolean, phoneNumberFieldValue: String) {
        appPreferencesHelper.setLoggedIn(validLogin)
        appPreferencesHelper.setLoggedInUserMobile(phoneNumberFieldValue)
    }

    private fun updateLoginButtonStatus(isEnable: Boolean) {
        if (isViewAttached()) {
            getView()?.updateLoginButtonStatus(isEnable)
        }
    }

    private fun updateVerifyOtpButtonStatus(isValidOtpFormat: Boolean) {
        if (isViewAttached()) {
            getView()!!.updateVerifyOtpButtonStatus(isValidOtpFormat)
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

    init {
        this.compositeDisposable = CompositeDisposable()
        this.loginInteractor = LoginInteractor(context, appPreferencesHelper)
    }
}