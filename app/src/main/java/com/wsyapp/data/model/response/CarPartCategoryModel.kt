package com.wsyapp.data.model.response

import android.content.Context
import com.wsyapp.utils.LocalizationPref

data class CarPartCategoryModel(
    val id: String,
    val name: String,
    val name_ar: String,
    var isSelected: Boolean = false
) {
    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else name_ar ?: ""
    }
}