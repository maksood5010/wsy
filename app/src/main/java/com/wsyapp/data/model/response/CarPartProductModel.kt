package com.wsyapp.data.model.response

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.wsyapp.R
import com.wsyapp.utils.LocalizationPref

data class CarPartProductModel(
    val details: String?,
    val disable: String?,
    val hide: String?,
    val id: String?,
    val img: String?,
    val cat: String?,
    val name: String?,
    val name_ar: String?,
    val price: Double?,
    val rate: Float,
    val shop_id: String?,
    val weight: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else name_ar ?: ""
    }

    fun getPriceText(context: Context): String {
        return "" + price + " " + context.getString(R.string.aed)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(details)
        parcel.writeString(disable)
        parcel.writeString(hide)
        parcel.writeString(id)
        parcel.writeString(img)
        parcel.writeString(cat)
        parcel.writeString(name)
        parcel.writeString(name_ar)
        parcel.writeValue(price)
        parcel.writeFloat(rate)
        parcel.writeString(shop_id)
        parcel.writeString(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarPartProductModel> {
        override fun createFromParcel(parcel: Parcel): CarPartProductModel {
            return CarPartProductModel(parcel)
        }

        override fun newArray(size: Int): Array<CarPartProductModel?> {
            return arrayOfNulls(size)
        }
    }
}