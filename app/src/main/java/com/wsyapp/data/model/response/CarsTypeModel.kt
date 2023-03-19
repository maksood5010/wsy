package com.wsyapp.data.model.response

import android.content.Context
import com.wsyapp.utils.LocalizationPref

data class CarsTypeModel(val context: Context,
    val id: String,
    val img: String,
    val name: String,
    val name_ar: String
) {

    override fun toString(): String {
        return getTypeName(context)
    }

    fun getTypeName(context: Context): String {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name
        else name_ar
    }
}