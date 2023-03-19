package com.wsyapp.ui.home.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.response.GlobalResponseModel

class AddAddressViewModel :ViewModel() {
    private var liveData = MutableLiveData<MutableList<GlobalResponseModel>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }
    fun putAddres(
        city:String,
        area:String,
        phone:String,
        street:String,
        building:String,
        apartment:String
    ){

    }
}