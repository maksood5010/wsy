package com.wsyapp.data.model.response

data class CartItemsResponseModel(
    val products: MutableList<CartItemModel>?,
    val success: Boolean,
    val address: MutableList<AddressModel>?
)

