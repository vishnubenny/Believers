package com.fabsv.believers.believers.data.source.remote.retrofit

import com.fabsv.believers.believers.data.source.remote.model.CollectionReportResponse
import com.fabsv.believers.believers.data.source.remote.model.LoginResponse
import com.fabsv.believers.believers.data.source.remote.model.QuorumReportResponse
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    //    @GET("5b07b7363200007e006ffff2")
    @GET("ValidateLogin")
    fun userLogin(@Query("UserName") username: String,
                  @Query("Password") password: String,
                  @Query("MobileNumber") mobile: String): Observable<Response<LoginResponse>>

    //        @GET("5afc3f1a3100006e007c5d99")
    @GET("GetByQRCode")
    fun getUserProfile(@Query("QRCode") qrValue: String,
                       @Query("MandalamId") mandalamId: String?,
                       @Query("MeetingSlno") meetingSlNo: String?): Observable<Response<UserProfileResponse>>

    @POST("MakePresent")
    fun makeAttendancePresent(@Body requestBody: RequestBody): Observable<Response<Void>>

    //    @GET("5b0540623200008100ebf7b1")
    @GET("CollectionSummary")
    fun getCollectionReport(@Query("MandalamId") mandalamId: String,
                            @Query("MeetingSlno") meetingSlNo: String,
                            @Query("UserId") userId: String,
                            @Query("MobileNo") mobile: String): Observable<Response<CollectionReportResponse>>

    //        @GET("5b040f1f2f0000e017e7a869")
    @GET("Quorum")
    fun getQuorumReport(@Query("MandalamId") mandalamId: String,
                        @Query("MeetingSlno") meetingSlNo: String,
                        @Query("QuorumTime") dateTimeObject: String): Observable<Response<QuorumReportResponse>>
}