package com.wsyapp.data.model.response

import android.content.Context
import com.wsyapp.R
import com.wsyapp.utils.LocalizationPref

data class ProductModel(
    val bestseller: String,
    val details: String,
    val disable: String,
    val hide: String,
    val id: String,
    val img: String,
    val likes: String,
    val menu_cat: String,
    val name: String,
    val name_ar: String,
    val price: String,
    val rate: Float,
    val shop_id: String,
    val time: String,
    val weight: String,
    val quantity: Int
) {
    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else name_ar ?: ""
    }

    fun getPriceText(context: Context): String {
        return "" + price + " " + context.getString(R.string.aed)
    }
}