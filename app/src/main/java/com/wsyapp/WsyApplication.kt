package com.wsyapp

import android.app.Application
import android.util.Log

private const val TAG = "WsyApplication"

class WsyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ")
    }

/*    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocalizationUtil.applyLanguage(base!!))
    }*/
}