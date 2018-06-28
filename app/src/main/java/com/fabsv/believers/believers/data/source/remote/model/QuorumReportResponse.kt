package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QuorumReportResponse : Serializable {
    @SerializedName("ClergyCount")
    @Expose
    var clergyCount: Int? = null

    @SerializedName("ClergyPresentCount")
    @Expose
    var clergyPresentCount: Int? = null

    @SerializedName("ClergyPerc")
    @Expose
    var clergyPerc: Float? = null

    @SerializedName("LaymenCount")
    @Expose
    var laymenCount: Int? = null

    @SerializedName("LaymenPresentCount")
    @Expose
    var laymenPresentCount: Int? = null

    @SerializedName("LaymenPerc")
    @Expose
    var laymenPerc: Float? = null

    @SerializedName("TotalMemberCount")
    @Expose
    var totalMemberCount: Int? = null

    @SerializedName("PresentMemberCount")
    @Expose
    var presentMemberCount: Int? = null

    @SerializedName("QuorumPerc")
    @Expose
    var quorumPerc: Float? = null
}