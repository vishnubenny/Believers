package com.fabsv.believers.believers.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabsv.believers.believers.App
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(getLayoutResId(), container, false)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        (activity as MvpActivity<*, *>).showFragment(fragment, isAddToBackStack)
    }

    override fun getAppPreferencesHelper(): AppPreferencesHelper {
        appPreferenceHelper = (mActivity!!.applicationContext as App).getAppPreferencesHelper()
        return appPreferenceHelper
    }

    override fun popBackCurrentFragment(): Boolean {
        return (activity as MvpActivity<*, *>).popBackCurrentFragment()
    }

    override fun hasPermission(permission: String): Boolean {
        return (activity as MvpActivity<*, *>).hasPermission(permission)
    }

    override fun requestPermissionsSafely(permissionArray: Array<String>, requestId: Int) {
        (activity as MvpActivity<*, *>).requestPermissionsSafely(permissionArray, requestId)
    }

    override fun safeFinishActivity() {
        (activity as MvpActivity<*, *>).safeFinishActivity()
    }

    override fun showShortToast(message: String) {
        activity!!.toast(message)
    }
}