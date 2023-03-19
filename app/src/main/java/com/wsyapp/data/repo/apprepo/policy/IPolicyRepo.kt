package com.wsyapp.data.repo.apprepo.policy

import com.wsyapp.data.model.request.PolicyRequestModel
import com.wsyapp.data.model.response.PolicyResponseModel
import com.wsyapp.data.repo.repo_base.Repocallback

interface IPolicyRepo {
    fun getPolicy(faqRequestModel: PolicyRequestModel, callback: Repocallback<PolicyResponseModel>)

}