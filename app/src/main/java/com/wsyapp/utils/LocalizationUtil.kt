package com.wsyapp.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*


private const val TAG = "LocalizationUtil"

object LocalizationUtil {

    /* fun applyLanguage(context: Context): Context {
        val language = LocalizationPref(context).getCurrentLanguage()
         val locale = Locale(language)
         val configuration = context.resources.configuration
         val displayMetrics = context.resources.displayMetrics
         Locale.setDefault(locale)
         configuration.locale=locale
         context.resources.updateConfiguration(configuration, displayMetrics)
        return context
        *//* return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            configuration.setLocale(locale)
            Log.e(TAG, "applyLanguage if: "+language )
            context.createConfigurationContext(configuration)
        } else {
            Log.e(TAG, "applyLanguage else: "+language )
            configuration.locale = locale
            context.resources.updateConfiguration(configuration, displayMetrics)
            context
        }*//*
    }*/

    fun applyLanguage(context: Context) {
        val language = LocalizationPref(context).getCurrentLanguage()

        val activityRes: Resources = context.resources
        val activityConf: Configuration = activityRes.configuration
        val newLocale = Locale(language)
        activityConf.setLocale(newLocale)
        activityConf.setLayoutDirection(newLocale)
        activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)
        val applicationRes: Resources =
            context.applicationContext.resources
        val applicationConf: Configuration = applicationRes.getConfiguration()
        applicationConf.setLocale(newLocale)
        applicationRes.updateConfiguration(
            applicationConf,
            applicationRes.displayMetrics
        )
    }


}