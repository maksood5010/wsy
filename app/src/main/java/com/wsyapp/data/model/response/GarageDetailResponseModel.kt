package com.wsyapp.data.model.response

data class GarageDetailResponseModel(
    val garage: List<GarageDetailModel>,
    val services: List<GarageServiceModel>,
    val slider: MutableList<GarageSliderModel>
)