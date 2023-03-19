package com.wsyapp.data.database.entity

import android.content.Context
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wsyapp.R
import com.wsyapp.utils.LocalizationPref
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "bestseller")
    var bestseller: String,

    @ColumnInfo(name = "details")
    var details: String,

    @ColumnInfo(name = "disable")
    var disable: String,

    @ColumnInfo(name = "hide")
    var hide: String,

    @ColumnInfo(name = "id_product")
    var id_product: String,
    @ColumnInfo(name = "img")
    var img: String,
    @ColumnInfo(name = "likes")
    var likes: String,

    @ColumnInfo(name = "menu_cat")
    var menu_cat: String,

    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "name_ar")
    var name_ar: String,
    @ColumnInfo(name = "price")
    var price: String,
    @ColumnInfo(name = "rate")
    var rate: Float,
//    @ColumnInfo(name = "singleprice")
//    var singleRate: String,
    @ColumnInfo(name = "shop_id")
    var shop_id: String,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "weight")
    var weight: String,
    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "stock_quantity")
    var stock_quantity: Int
) : Parcelable {

    constructor() : this(0, "", "", "", "", "", "", "", "", "", "", "", 0.0F, "", "", "", 0, 0)

    fun getName(context: Context): String? {
        val currentLanguageEnglish = LocalizationPref(context).isCurrentLanguageEnglish()
        return if (currentLanguageEnglish) name ?: ""
        else name_ar ?: ""
    }

    fun getPriceText(context: Context): String {
        return "" + price + " " + context.getString(R.string.aed)
    }
    fun getPriceTotal(context: Context,model: Cart): String {
        val itemQty = model.quantity
        val itemPrice = model.price.toDouble()
        val total = itemPrice * itemQty
        val number2digits: Double = String.format("%.2f", total).toDouble()
        return (number2digits.toString()+ " " + context.getString(R.string.aed))
    }

}