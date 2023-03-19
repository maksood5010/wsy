package com.wsyapp.data.model.response

data class CarDetailsResponseModel(
    val cars: List<CarModel>,
    val success: Boolean
)