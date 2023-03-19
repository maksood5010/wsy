package com.wsyapp.data.model.response

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.wsyapp.utils.LocalizationPref

data class GarageCategoryModel(
    val hide: String?,
    val id: String?,
    val img: String?,
    val name: String?,
    val name_ar: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hide)
        parcel.writeString(id)
        parcel.writeString(img)
        parcel.writeString(name)
        parcel.writeString(name_ar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GarageCategoryModel> {
        override fun createFromParcel(parcel: Parcel): GarageCategoryModel {
            return GarageCategoryModel(parcel)
        }

        override fun newArray(size: Int): Array<GarageCategoryModel?> {
            return arrayOfNulls(size)
        }
    }

    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else name_ar ?: ""
    }
}
