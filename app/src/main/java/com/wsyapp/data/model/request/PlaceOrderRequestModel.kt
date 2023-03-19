package com.wsyapp.data.model.request

import com.wsyapp.data.model.response.AddressForPlaceOrderModel

data class PlaceOrderRequestModel(
    val st: Int,
    val user_id: String,
    val phone: String,
    val address: MutableList<AddressForPlaceOrderModel>?,
    val ddate: String,
    val payment: Int,
    val delivery: String,
    val total: String,
    val device: String,
    val orders: MutableList<OrderRequestModel>
)