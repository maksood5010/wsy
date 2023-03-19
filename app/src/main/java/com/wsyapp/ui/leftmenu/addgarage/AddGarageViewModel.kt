package com.wsyapp.ui.leftmenu.addgarage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.AddGarageRequestModel
import com.wsyapp.data.model.response.GarageCategoryResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.apprepo.garage.GarageRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class AddGarageViewModel : ViewModel() {
    private var liveData = MutableLiveData<Resource<GarageCategoryResponseModel, Action>>()
    private var add_garage_liveData = MutableLiveData<Resource<GlobalResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getCategoryLiveData(): LiveData<Resource<GarageCategoryResponseModel, Action>> {
        return liveData
    }

    fun getaddGarageLiveData(): LiveData<Resource<GlobalResponseModel, Action>> {
        return add_garage_liveData
    }

    fun getGarageCategories(st: Int) {
        GarageRepo().getGarageCategories(st, object : Repocallback<GarageCategoryResponseModel> {
            override fun onResult(result: Resource<GarageCategoryResponseModel?, Resource.Status>) {
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

    fun addGarge(requestModel: AddGarageRequestModel) {
        GarageRepo().addGarage(requestModel, object : Repocallback<GlobalResponseModel> {
            override fun onResult(result: Resource<GlobalResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> add_garage_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> add_garage_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> add_garage_liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> add_garage_liveData.postValue(
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