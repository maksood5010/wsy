package com.wsyapp.data.model.response

import android.content.Context
import com.wsyapp.utils.LocalizationPref

data class SliderModel(
    val details: String,
    val details_ar: String,
    val hide: String,
    val id: String,
    val img: String,
    val title: String,
    val title_ar: String
){
    fun getDetails(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) details ?: ""
        else details_ar ?: ""
    }
}