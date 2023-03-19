package com.wsyapp.ui.home.garage.subcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.response.GarageSubCategoryResponseModel
import com.wsyapp.data.repo.apprepo.garage.GarageRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class GarageSubcategoryViewModel : ViewModel() {
    private var liveData = MutableLiveData<Resource<GarageSubCategoryResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<GarageSubCategoryResponseModel, Action>> {
        return liveData
    }

    fun getSubGarageCategories(st: Int, cat: Int) {
        GarageRepo().getSubGarageCategories(
            st,
            cat,
            object : Repocallback<GarageSubCategoryResponseModel> {
                override fun onResult(result: Resource<GarageSubCategoryResponseModel?, Resource.Status>) {
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