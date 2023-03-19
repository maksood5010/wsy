package com.wsyapp.data.model.response

data class HomeSliderResponseModel(
    val android_version: Int,
    val fupdateandroid: Boolean,
    val fupdateios: Boolean,
    val ios_version: Int,
    val slider: List<SliderModel>,
    val success: Boolean
)