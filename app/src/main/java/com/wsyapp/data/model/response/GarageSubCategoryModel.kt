package com.wsyapp.data.model.response

import android.content.Context
import com.wsyapp.utils.LocalizationPref

data class GarageSubCategoryModel(
    val address: String,
    val ar_name: String,
    val cover: String,
    val featured: String,
    val id: String,
    val name: String,
    val rate: String,
    val reviews: String,
    val view: String,
    val city_id: String,
    var isSelected: Boolean = false
) {
    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else ar_name ?: ""
    }
}