package com.fabsv.believers.believers.ui.base

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabsv.believers.believers.App
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.ui.utils.inflate
import com.lv.note.personalnote.ui.base.BaseFragment
import com.lv.note.personalnote.ui.base.MvpPresenter

import org.jetbrains.anko.toast

/**
 * Created by vishnu.benny on 2/21/2018.
 */
abstract class MvpFragment<V : MvpView, P : MvpPresenter<V>> : BaseFragment(), MvpView {
    protected var presenter: P? = null
    private var mActivity: Activity? = null
    private lateinit var appPreferenceHelper: AppPreferencesHelper

    /**
     * Android extension utils is used
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container!!.inflate(getLayoutResId())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerPresenter()

        @Suppress("UNCHECKED_CAST")
        presenter!!.attachView(this as V)

        onPrepareFragment(view)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MvpActivity<*, *>) {
            mActivity = context
        }
    }

    override fun onResume() {
        super.onResume()
        hideSoftKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter!!.detachView(retainInstance)
    }

    abstract fun onPrepareFragment(view: View?)

    private fun registerPresenter() {
        if (null == presenter) {
            presenter = createPresenter()
        }
    }

    abstract fun createPresenter(): P?

    abstract fun getLayoutResId(): Int

    override fun showProgress() {
        (activity as MvpActivity<*, *>).showProgress()
    }

    override fun hideProgress() {
        (activity as MvpActivity<*, *>).hideProgress()
    }

    override fun showFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        (activity as MvpActivity<*, *>).showFragment(fragment, isAddToBackStack)
    }

    override fun hideSoftKeyboard() {
        (activity as MvpActivity<*, *>).hideSoftKeyboard()
    }

    override fun getAppPreferencesHelper(): AppPreferencesHelper {
        appPreferenceHelper = (mActivity!!.applicationContext as App).getAppPreferencesHelper()
        return appPreferenceHelper
    }

    override fun popBackCurrentFragment(): Boolean {
        return (activity as MvpActivity<*, *>).popBackCurrentFragment()
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun hasPermission(permission: String): Boolean {
        return (activity as MvpActivity<*, *>).hasPermission(permission)
    }

    override fun requestPermissionsSafely(permissionArray: Array<String>, requestId: Int) {
        (activity as MvpActivity<*, *>).requestPermissionsSafely(permissionArray, requestId)
    }

    override fun safeFinishActivity() {
        (activity as MvpActivity<*, *>).safeFinishActivity()
    }

    override fun updateToolbarTitle(title: String?, homeUpEnabled: Boolean) {
        title?.let {
            (activity as MvpActivity<*, *>).updateToolbarTitle(title, homeUpEnabled)
        }
    }

    override fun showShortToast(message: String) {
        activity!!.toast(message)
    }

    override fun showFailedShortToast(message: String) {
        (activity as MvpActivity<*, *>).showFailedShortToast(message)
    }

    override fun showSuccessShortToast(message: String) {
        (activity as MvpActivity<*, *>).showSuccessShortToast(message)
    }
}