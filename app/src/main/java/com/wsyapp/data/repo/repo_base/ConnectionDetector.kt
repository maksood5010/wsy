package com.wsyapp.data.repo.repo_base

import android.content.Context
import android.net.ConnectivityManager

object ConnectionDetector {

    fun isNetAvailable(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        val isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        return isConnected

    }
}