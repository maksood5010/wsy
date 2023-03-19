package com.wsyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.HomeRequestModel
import com.wsyapp.data.model.response.HomeSliderResponseModel
import com.wsyapp.data.repo.apprepo.home.HomeRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class HomeViewModel : ViewModel() {

    private  var liveData = MutableLiveData<Resource<HomeSliderResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<HomeSliderResponseModel, Action>> {
        return liveData
    }

    fun gethomeSlider(requsetModel: HomeRequestModel) {
        HomeRepo().gethomeSlider(requsetModel, object : Repocallback<HomeSliderResponseModel> {
            override fun onResult(result: Resource<HomeSliderResponseModel?, Resource.Status>) {
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