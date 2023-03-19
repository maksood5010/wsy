package com.wsyapp.ui.right_menu.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.model.request.PolicyRequestModel
import com.wsyapp.data.model.response.PolicyResponseModel
import com.wsyapp.data.repo.apprepo.policy.PolicyRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

private const val TAG = "FaqViewModel"
class FaqViewModel : ViewModel() {

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    private var liveData = MutableLiveData<Resource<PolicyResponseModel, Action>>()

    fun getLiveData(): LiveData<Resource<PolicyResponseModel, Action>> {
        return liveData
    }

    fun getFaqFromServer(faqRequestModel: PolicyRequestModel) {
        PolicyRepo()
            .getPolicy(faqRequestModel, object :
            Repocallback<PolicyResponseModel> {
            override fun onResult(result: Resource<PolicyResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> {
                        val payload = result.payload
                        liveData.postValue(
                            Resource(
                                result.payload,
                                Action.SUCCESS,
                                ""
                            )
                        )
                    }
                    Resource.Status.SERVER_ERROR -> {
                        liveData.postValue(
                            Resource(
                                result.payload,
                                Action.SERVER_ERROR,
                                ""
                            )
                        )
                    }
                    Resource.Status.NOT_FOUND -> {
                        liveData.postValue(
                            Resource(
                                result.payload,
                                Action.NOT_FOUND,
                                ""
                            )
                        )
                    }
                    else -> {
                        liveData.postValue(
                            Resource(
                                result.payload,
                                Action.NETWORK_ERROR,
                                ""
                            )
                        )
                    }
                }
            }


        })
    }
}