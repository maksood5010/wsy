package com.wsyapp.data.model.request

import android.os.Parcel
import android.os.Parcelable

class LoginRequestModel(
    val name: String?,
    val email: String?,
    val phone: String?,
    val phone_number: String?,
    val verificationId: String?
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
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(phone_number)
        parcel.writeString(verificationId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginRequestModel> {
        override fun createFromParcel(parcel: Parcel): LoginRequestModel {
            return LoginRequestModel(parcel)
        }

        override fun newArray(size: Int): Array<LoginRequestModel?> {
            return arrayOfNulls(size)
        }
    }

}