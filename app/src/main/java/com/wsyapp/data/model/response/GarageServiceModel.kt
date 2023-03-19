package com.wsyapp.data.model.response

import android.content.Context
import com.wsyapp.utils.LocalizationPref

data class GarageServiceModel(
    val disable: String,
    val garage_id: String,
    val hide: String,
    val id: String,
    val img: String,
    val name: String,
    val name_ar: String,
    val price: Double,
    val rate: String
) {
    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else name_ar ?: ""
    }
}