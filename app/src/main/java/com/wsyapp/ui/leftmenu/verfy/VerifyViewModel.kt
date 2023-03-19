package com.wsyapp.ui.leftmenu.verfy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.VerifyRequestModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.apprepo.login.LoginRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class VerifyViewModel :ViewModel(){
    private var liveData = MutableLiveData<Resource<GlobalResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<GlobalResponseModel, Action>> {
        return liveData
    }

    fun verifyUser(requsetModel: VerifyRequestModel) {
        LoginRepo().verifyUser(requsetModel, object : Repocallback<GlobalResponseModel> {
            override fun onResult(result: Resource<GlobalResponseModel?, Resource.Status>) {
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