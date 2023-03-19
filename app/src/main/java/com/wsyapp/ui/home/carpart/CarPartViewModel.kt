package com.wsyapp.ui.home.carpart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.response.CarPartResponseModel
import com.wsyapp.data.repo.apprepo.carpart.CarPartRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class CarPartViewModel : ViewModel() {

    private var liveData = MutableLiveData<Resource<CarPartResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<CarPartResponseModel, Action>> {
        return liveData
    }

    fun getCarPartProducs(st: Int) {
        CarPartRepo().getCarPartProducs(st, object : Repocallback<CarPartResponseModel> {
            override fun onResult(result: Resource<CarPartResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> liveData.postValue(
                        Resource(
                            result.payload,
                            Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> liveData.postValue(Resource(result.payload, Action.NETWORK_ERROR, ""))

                }
            }

        })
    }
}