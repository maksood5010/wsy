package com.wsyapp.ui.home.garage.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.GarageDetailRequestModel
import com.wsyapp.data.model.response.GarageDetailResponseModel
import com.wsyapp.data.repo.apprepo.garage.GarageRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class GarageDetailViewModel : ViewModel() {
    private var liveData = MutableLiveData<Resource<GarageDetailResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<GarageDetailResponseModel, Action>> {
        return liveData
    }

    fun getGarageDetail(requestModel: GarageDetailRequestModel) {
        GarageRepo().getGarageDetail(
            requestModel,
            object : Repocallback<GarageDetailResponseModel> {
                override fun onResult(result: Resource<GarageDetailResponseModel?, Resource.Status>) {
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
                        else -> liveData.postValue(
                            Resource(
                                result.payload,
                                Action.NETWORK_ERROR,
                                ""
                            )
                        )

                    }
                }

            })
    }

}