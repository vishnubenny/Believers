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
//    @GET("5b2b77e73000007e0023464b")    //login mock
    @GET("ValidateLogin")
    fun userLogin(@Query("UserName") username: String,
                  @Query("Password") password: String,
                  @Query("MobileNumber") mobile: String): Observable<Response<LoginResponse>>

//    @GET("5b0ade4e2f00009800ec4c63") // empty data
//    @GET("5b0faa3b3000004a00115186") // full data: attType: Not Present
//    @GET("5b33a3a1320000771bd1e1ef") //attType: Present response
    @GET("GetByQRCode")
    fun getUserProfile(@Query("QRCode") qrValue: String,
                       @Query("MandalamId") mandalamId: String?,
                       @Query("MeetingSlno") meetingSlNo: String?): Observable<Response<UserProfileResponse>>

//    @GET("5b0faa3b3000004a00115186") // full data: attType: Not Present
    @GET("GetByMemberCode")
    fun getUserProfileFromSearch(@Query("QRCode") qrValue: String,
                                 @Query("MandalamId") mandalamId: String,
                                 @Query("MeetingSlno") meetingSlNo: String):
            Observable<Response<UserProfileResponse>>

    @POST("MakePresent")
    fun makeAttendancePresent(@Body requestBody: RequestBody): Observable<Response<Void>>

//        @GET("5b0540623200008100ebf7b1")
    @GET("CollectionSummary")
    fun getCollectionReport(@Query("MandalamId") mandalamId: String,
                            @Query("MeetingSlno") meetingSlNo: String,
                            @Query("UserId") userId: String,
                            @Query("MobileNo") mobile: String): Observable<Response<CollectionReportResponse>>


    /*Updated quorum report query as per the instruction on 26/06/2017.
    No more "QuorumTime" field is needed in the get request*/
//    @GET("5b34c41a2f00004e0037607e") // full data
    @GET("Quorum")
    fun getQuorumReport(@Query("MandalamId") mandalamId: String,
                        @Query("MeetingSlno") meetingSlNo: String): Observable<Response<QuorumReportResponse>>
//                        @Query("MeetingSlno") meetingSlNo: String,
//                        @Query("QuorumTime") dateTimeObject: String): Observable<Response<QuorumReportResponse>>

}