package com.wsyapp.data.model.request

data class EditCarRequestModel(
    val st: Int,
    val name: String,
    val user_id: String,
    val model: String,
    val plate: String,
    val id: String,
    val image: String,
    val car: String,
    val type: String
)