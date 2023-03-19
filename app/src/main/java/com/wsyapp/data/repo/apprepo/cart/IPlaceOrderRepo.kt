package com.wsyapp.data.repo.apprepo.cart

import com.wsyapp.data.model.request.PlaceOrderRequestModel
import com.wsyapp.data.model.response.PlaceOrderResponseModel
import com.wsyapp.data.repo.repo_base.Repocallback


interface IPlaceOrderRepo {
    fun placeOrder(placeOrderRequestModel: PlaceOrderRequestModel, callback: Repocallback<PlaceOrderResponseModel>)
}