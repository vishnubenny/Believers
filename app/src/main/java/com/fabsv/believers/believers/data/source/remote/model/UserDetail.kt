package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDetail {
    @SerializedName("name")
    @Expose
    private var name: String? = null
        get() = field
        set(value) {
            field = value
        }

    @SerializedName("image")
    @Expose
    private var image: String? = null
        get() = field
        set(value) {
            field = value
        }

    @SerializedName("address")
    @Expose
    private var address: String? = null
        get() = field
        set(value) {
            field = value
        }
}