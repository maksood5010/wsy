package com.wsyapp.ui.leftmenu.profile.cars.addcar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.AddCarRequestModel
import com.wsyapp.data.model.request.EditCarRequestModel
import com.wsyapp.data.model.response.CarTypeResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.apprepo.profile.addcar.CarRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class AddCarViewModel : ViewModel() {
    enum class Action {
        SUCCESS, NETWORK_ERROR, NOT_FOUND, SERVER_ERROR
    }

    private var add_liveData = MutableLiveData<Resource<GlobalResponseModel, Action>>()
    private var edit_liveData = MutableLiveData<Resource<GlobalResponseModel, Action>>()
    private var carType_liveData = MutableLiveData<Resource<CarTypeResponseModel, Action>>()

    fun getAddCarLiveData(): LiveData<Resource<GlobalResponseModel, Action>> {
        return add_liveData
    }

    fun getEditCarLiveData(): LiveData<Resource<GlobalResponseModel, Action>> {
        return edit_liveData
    }

    fun getCarTypeLiveData(): LiveData<Resource<CarTypeResponseModel, Action>> {
        return carType_liveData
    }

    fun addCar(requestModel: AddCarRequestModel) {
        CarRepo().addCar(requestModel, object : Repocallback<GlobalResponseModel> {
            override fun onResult(result: Resource<GlobalResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> add_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> add_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> add_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> add_liveData.postValue(
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

    fun editCar(
        requestModel: EditCarRequestModel
    ) {
        CarRepo().editCar(requestModel, object : Repocallback<GlobalResponseModel> {
            override fun onResult(result: Resource<GlobalResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> edit_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> edit_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> edit_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> edit_liveData.postValue(
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

    fun getCarType(st: Int) {
        CarRepo().getCarType(st, object : Repocallback<CarTypeResponseModel> {
            override fun onResult(result: Resource<CarTypeResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> carType_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> carType_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> carType_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> carType_liveData.postValue(
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