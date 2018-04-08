package com.fabsv.believers.believers.data.source.remote.retrofit

import com.fabsv.believers.believers.data.source.remote.model.User
import com.fabsv.believers.believers.data.source.remote.model.UserDetail
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("login")
    fun userLogin(@Query("phone") phoneNumber: String): Observable<User>

    @GET("getdata")
    fun getUserData(@Query("qrvalue") qrValue: String): Observable<UserDetail>

    @GET("attendance")
    fun updateApproveStatusOfUser(@Query("phone") phoneNumber: String,
                                  @Query("qrvalue") qrValue: String,
                                  @Query("status") updateStatus: String): Observable<User>
}