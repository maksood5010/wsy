package com.wsyapp.data.model.response

data class AddressModel (
    val id: String,
    val uid: String,
    val phone: String,
    val lat: String,
    val lon: String,
    val address: String,
    val area: String,
    val street: String,
    val building: String,
    val apartment: String,
    val floor: String
)
//[{id,uid,phone,lat,lon,address,area,st,building,
//    apartment,floor}]

