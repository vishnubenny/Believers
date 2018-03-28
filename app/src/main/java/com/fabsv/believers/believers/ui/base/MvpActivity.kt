package com.fabsv.believers.believers.ui.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.fabsv.believers.believers.App
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.lv.note.personalnote.ui.base.BaseActivity
import com.lv.note.personalnote.ui.base.MvpPresenter
import com.lv.note.personalnote.ui.base.MvpView
import org.jetbrains.anko.toast

/**
 * Created by vishnu.benny on 2/20/2018.
 */
abstract class MvpActivity<V : MvpView, P : MvpPresenter<V>> : BaseActivity(), MvpView {

    protected var presenter: P? = null
    private lateinit var appPreferenceHelper: AppPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        registerPresenter()

        @Suppress("UNCHECKED_CAST")
        presenter!!.attachView(this as V)
        onPrepareActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.detachView(false)
    }

    protected abstract fun onPrepareActivity()

    private fun registerPresenter() {
        if (null == presenter) {
            presenter = createPresenter()
        }
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showShortToast(message: String) {
        toast(message)
    }

    override fun showFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        if (isAddToBackStack) {
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
                    .addToBackStack(fragment.tag).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }
    }


    override fun getAppPreferencesHelper(): AppPreferencesHelper {
        appPreferenceHelper = (applicationContext as App).getAppPreferencesHelper()
        return appPreferenceHelper
    }

    override fun popBackCurrentFragment(): Boolean {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return true
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun requestPermissionsSafely(permissionArray: Array<String>, requestId: Int) {
        requestPermissions(permissionArray, requestId)
    }

    fun getCurrentFragmentInstance(): Fragment {
        return supportFragmentManager.findFragmentById(R.id.container)
    }

    protected abstract fun createPresenter(): P?

    protected abstract fun getLayoutResId(): Int

}