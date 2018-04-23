package com.fabsv.believers.believers.data.source.remote.retrofit

class ServiceGenerator {
    companion object {
        fun <S> createService(serviceClass: Class<S>) : S {
            val retrofit = ApiClient.getClient()
            return retrofit!!.create(serviceClass)
        }
    }
}