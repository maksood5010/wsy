package com.wsyapp.ui.leftmenu.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.response.ProfileResponseModel
import com.wsyapp.data.repo.apprepo.login.LoginRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class ProfileViewModel : ViewModel() {

    private var liveData = MutableLiveData<Resource<ProfileResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<ProfileResponseModel, Action>> {
        return liveData
    }

    fun getUserProfile(user_id: String) {
        LoginRepo().getUserProfile(user_id, object : Repocallback<ProfileResponseModel> {
            override fun onResult(result: Resource<ProfileResponseModel?, Resource.Status>) {
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
