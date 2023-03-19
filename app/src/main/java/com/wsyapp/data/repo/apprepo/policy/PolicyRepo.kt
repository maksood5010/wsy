package com.wsyapp.data.repo.apprepo.policy

import com.wsyapp.data.model.request.PolicyRequestModel
import com.wsyapp.data.model.response.PolicyResponseModel
import com.wsyapp.data.repo.repo_base.CallbackWrapper
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import com.wsyapp.data.repo.repo_base.RetrofitServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class PolicyRepo : IPolicyRepo {
    override fun getPolicy(
        faqRequestModel: PolicyRequestModel,
        callback: Repocallback<PolicyResponseModel>
    ) {
        val observable = RetrofitServiceBuilder.getApiService()
            .getFaq(faqRequestModel.st, faqRequestModel.data, faqRequestModel.lg)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(
            CallbackWrapper(object : CallbackWrapper.HttpHandler<PolicyResponseModel>(callback) {
                    override fun onSuccess(t: PolicyResponseModel?) {
                        callback.onResult(
                            Resource(
                                t,
                                Resource.Status.SUCCESS,
                                ""
                            )
                        )

                    }

                    override fun onCreated(t: PolicyResponseModel?) {

                    }

                    override fun onServerError() {
                        callback.onResult(
                            Resource(
                                null,
                                Resource.Status.SERVER_ERROR,
                                ""
                            )
                        )
                        super.onServerError()
                    }

                })
        )
    }

}