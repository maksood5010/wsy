package com.wsyapp.data.model.response

import android.content.Context
import com.wsyapp.utils.LocalizationPref

data class GarageDetailModel(
    val address: String,
    val ar_name: String,
    val category_id: String,
    val cover: String,
    val created_at: String,
    val createdby: String,
    val description: String,
    val email: String,
    val expire: String,
    val featured: String,
    val hours: String,
    val id: String,
    val inapp: String,
    val lat: String,
    val lon: String,
    val name: String,
    val pass: String,
    val phone: String,
    val rate: Float,
    val reviews: String,
    val status: String,
    val view: String
){
    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else ar_name ?: ""
    }
}