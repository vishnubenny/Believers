package com.fabsv.believers.believers.data.source.remote.retrofit

import com.fabsv.believers.believers.data.source.remote.model.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("test")
    fun userLogin(@Query("q") phoneNumber: String): Observable<User>
}