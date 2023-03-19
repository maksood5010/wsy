package com.wsyapp.ui.home.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.PlaceOrderRequestModel
import com.wsyapp.data.model.response.PlaceOrderResponseModel
import com.wsyapp.data.repo.apprepo.cart.PlaceOrderRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class TicketViewModel : ViewModel() {

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    private var liveData = MutableLiveData<Resource<PlaceOrderResponseModel, Action>>()

    fun getLiveData(): LiveData<Resource<PlaceOrderResponseModel, Action>> {
        return liveData
    }

    fun orderTicket(ticket: PlaceOrderRequestModel) {
        PlaceOrderRepo()
            .placeOrder(ticket,object :
                Repocallback<PlaceOrderResponseModel> {
                override fun onResult(result: Resource<PlaceOrderResponseModel?, Resource.Status>) {
                    when (result.action) {
                        Resource.Status.SUCCESS -> {
                            val payload = result.payload
                            liveData.postValue(
                                Resource(
                                    result.payload,
                                    Action.SUCCESS,
                                    ""
                                )
                            )
                        }
                        Resource.Status.SERVER_ERROR -> {
                            liveData.postValue(
                                Resource(
                                    result.payload,
                                    Action.SERVER_ERROR,
                                    ""
                                )
                            )
                        }
                        Resource.Status.NOT_FOUND -> {
                            liveData.postValue(
                                Resource(
                                    result.payload,
                                    Action.NOT_FOUND,
                                    ""
                                )
                            )
                        }
                        else -> {
                            liveData.postValue(
                                Resource(
                                    result.payload,
                                    Action.NETWORK_ERROR,
                                    ""
                                )
                            )
                        }
                    }
                }


            })
    }

}