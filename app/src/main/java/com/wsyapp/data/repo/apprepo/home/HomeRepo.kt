package com.wsyapp.data.repo.apprepo.home

import com.wsyapp.data.model.request.HomeRequestModel
import com.wsyapp.data.model.response.HomeSliderResponseModel
import com.wsyapp.data.repo.repo_base.CallbackWrapper
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import com.wsyapp.data.repo.repo_base.RetrofitServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeRepo : IHomeRepo {
    override fun gethomeSlider(
        requestModel: HomeRequestModel,
        callback: Repocallback<HomeSliderResponseModel>
    ) {
        val observeOn = RetrofitServiceBuilder.getApiService()
            .getHomeSlider(requestModel.st, requestModel.version, requestModel.user_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(
            CallbackWrapper(object :
                CallbackWrapper.HttpHandler<HomeSliderResponseModel>(callback) {
                override fun onSuccess(t: HomeSliderResponseModel?) {
                    callback.onResult(
                        Resource(
                            t,
                            Resource.Status.SUCCESS,
                            ""
                        )
                    )

                }

                override fun onCreated(t: HomeSliderResponseModel?) {

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