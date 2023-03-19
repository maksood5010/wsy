package com.wsyapp.data.model.response

data class CarPartResponseModel(
    val categories: List<CarPartCategoryModel>?,
    val products: List<CarPartProductModel>?
)