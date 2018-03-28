package com.fabsv.believers.believers.ui.module.userdetail

import android.content.Context
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.lv.note.personalnote.ui.base.MvpBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class UserDetailPresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) : MvpBasePresenter<UserDetailContract.UserDetailView>(), UserDetailContract.UserDetailPresenter {

    private var compositeDisposable: CompositeDisposable

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    override fun validate() {
        val rejectButtonObservable: Observable<Boolean> = getView()!!
                .getRejectButtonClickEvent()
                .map { event: Any -> true }
                .doOnNext { exitUserDetailScreen() }
        val rejectButtonDisposable = rejectButtonObservable.subscribe()
        this.compositeDisposable.add(rejectButtonDisposable)
    }

    private fun exitUserDetailScreen() {
        if (isViewAttached()) {
            getView()!!.exitUserDetailScreen()
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
        disposeCompositeDisposable()
        super.detachView(retainInstance)
    }
}