package com.wsyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.wsyapp.data.repo.RepoConstant

class LocalizationPref(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val PREF_NAME = "wsy_locale"
    private val KEY_CUR_LANG = "current_lang"


    init {

        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveCurrentLanguage(language: String) {
        val edit = sharedPreferences.edit()
        edit.putString(KEY_CUR_LANG, language)
        edit.apply()
    }

    fun getCurrentLanguage(): String {
        val string = sharedPreferences.getString(KEY_CUR_LANG, MyConstants.KEY_ENGLISH)
        return string!!
    }

    fun getCurrentLanguageForApi(): String {
        val string = sharedPreferences.getString(KEY_CUR_LANG, MyConstants.KEY_ENGLISH)
        return when (string) {
            MyConstants.KEY_ARABIC -> RepoConstant.ARABIC
            else -> RepoConstant.ENGLISH
        }
    }

    fun isCurrentLanguageEnglish(): Boolean {
        val string = getCurrentLanguageForApi()
        return when (string) {
            MyConstants.KEY_ARABIC -> false
            else -> true
        }
    }

}