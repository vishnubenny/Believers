package com.fabsv.believers.believers.data.source.remote.retrofit

import com.fabsv.believers.believers.data.source.remote.model.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @GET("ValidateLogin")
    fun userLogin(@Query("UserName") username: String,
                  @Query("Password") password: String,
                  @Query("MobileNumber") mobile: String): Observable<Response<LoginResponse>>

    //    @GET("5afc3f1a3100006e007c5d99")
    @GET("GetByQRCode")
    fun getUserProfile(@Query("QRCode") qrValue: String,
                       @Query("MandalamId") mandalamId: String?,
                       @Query("MeetingSlno") meetingSlNo: String?): Observable<Response<UserProfileResponse>>

    @POST("MakePresent")
    @FormUrlEncoded
    fun makeAttendancePresent(@Body makeAttendancePresentModel: MakeAttendancePresentModel?): Observable<Response<Any>>

//    @GET("5b0406a82f0000e017e7a83b")
    @GET("CollectionSummary")
    fun getCollectionReport(@Query("MandalamId") mandalamId: String,
                            @Query("MeetingSlno") meetingSlNo: String,
                            @Query("UserId") userId: String,
                            @Query("MobileNo") mobile: String): Observable<Response<CollectionReportResponse>>

    //    @GET("5b040f1f2f0000e017e7a869")
    @GET("Quorum")
    fun getQuorumReport(@Query("MandalamId") mandalamId: String,
                        @Query("MeetingSlno") meetingSlNo: String,
                        @Query("QuorumTime") dateTimeObject: String): Observable<Response<QuorumReportResponse>>
}