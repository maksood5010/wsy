package com.wsyapp.ui.home.garage.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.response.GarageCategoryResponseModel
import com.wsyapp.data.repo.apprepo.garage.GarageRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class CategoryViewModel :ViewModel() {

    private  var liveData = MutableLiveData<Resource<GarageCategoryResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<GarageCategoryResponseModel, Action>> {
        return liveData
    }

    fun getGarageCategories(st:Int) {
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
}