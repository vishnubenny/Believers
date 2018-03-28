package com.fabsv.believers.believers.data.source

import io.reactivex.Observable

interface UserDataSource {
    fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean>
    fun onLogoutClicked(): Observable<Boolean>
}