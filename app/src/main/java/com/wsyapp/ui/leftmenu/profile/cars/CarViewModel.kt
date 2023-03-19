package com.wsyapp.ui.leftmenu.profile.cars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.CarDetailsRequestModel
import com.wsyapp.data.model.request.DeleteCarRequestModel
import com.wsyapp.data.model.response.CarDetailsResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.apprepo.profile.addcar.CarRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class CarViewModel : ViewModel() {

    enum class Action {
        SUCCESS, NETWORK_ERROR, NOT_FOUND, SERVER_ERROR
    }

    private var liveData = MutableLiveData<Resource<CarDetailsResponseModel, Action>>()
    private var delete_liveData = MutableLiveData<Resource<GlobalResponseModel, Action>>()

    fun getLiveData(): LiveData<Resource<CarDetailsResponseModel, Action>> {
        return liveData
    }


    fun getDeleteCarLiveData():LiveData<Resource<GlobalResponseModel, Action>>{
        return delete_liveData
    }
    fun getCarDetails(
        requestModel: CarDetailsRequestModel
    ) {
        CarRepo().getCarDetails(requestModel, object : Repocallback<CarDetailsResponseModel> {
            override fun onResult(result: Resource<CarDetailsResponseModel?, Resource.Status>) {
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

    fun deleteCar(requestModel: DeleteCarRequestModel) {
        CarRepo().deleteCar(requestModel, object : Repocallback<GlobalResponseModel> {
            override fun onResult(result: Resource<GlobalResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> delete_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> delete_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> delete_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> delete_liveData.postValue(
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