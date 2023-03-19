package com.wsyapp.data.model.request

data class OrderRequestModel(
    val shop_id: String,
    var id: String,
    val price: String,
    val quantity: String
)
