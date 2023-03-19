package com.wsyapp.data.model.response

import android.os.Parcel
import android.os.Parcelable

data class CarModel(
    val car: String?,
    val date: String?,
    val hide: String?,
    val id: String?,
    val img: String?,
    val model: String?,
    val name: String?,
    val plate: String?,
    val isdefault: Boolean?,
    val user_id: String?,
    val type_id: String?
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
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(car)
        parcel.writeString(date)
        parcel.writeString(hide)
        parcel.writeString(id)
        parcel.writeString(img)
        parcel.writeString(model)
        parcel.writeString(name)
        parcel.writeString(plate)
        parcel.writeByte(if (isdefault!!) 1 else 0)
        parcel.writeString(user_id)
        parcel.writeString(type_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarModel> {
        override fun createFromParcel(parcel: Parcel): CarModel {
            return CarModel(parcel)
        }

        override fun newArray(size: Int): Array<CarModel?> {
            return arrayOfNulls(size)
        }
    }
}