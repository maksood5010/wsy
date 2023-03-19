package com.wsyapp.data.repo.repo_base

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

object RequestInterceptor : Interceptor {
    private const val TAG = "RequestInterceptor"
    var accessToken: String = ""
    var uploadAccessToken: String = ""
    private var CACHE_MAX_AGE: String? = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e(TAG, "accessToken :$accessToken ")
        Log.e(TAG, "uploadAccessToken :$uploadAccessToken ")
        val request = chain.request()
        var tempToken =
            accessToken
        Log.e(TAG, "Url path ${request.url().encodedPath()!!}")
        if (request.url().encodedPath()!!.contentEquals("/record")) {
            tempToken =
                uploadAccessToken
            Log.e(TAG, "Loading upload token ")
        }
        val modifiedRequest = request.newBuilder() //.addHeader("Authorization", "Bearer $tempToken").addHeader("2ndkey,", "2ndvalue")
        return chain.proceed(modifiedRequest.build())
    }
}