package com.wsyapp.data.repo.apprepo.garage

import com.wsyapp.data.model.request.AddGarageRequestModel
import com.wsyapp.data.model.request.GarageDetailRequestModel
import com.wsyapp.data.model.response.GarageCategoryResponseModel
import com.wsyapp.data.model.response.GarageDetailResponseModel
import com.wsyapp.data.model.response.GarageSubCategoryResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.RepoConstant.Companion.API_ADD_GARGE
import com.wsyapp.data.repo.repo_base.CallbackWrapper
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import com.wsyapp.data.repo.repo_base.RetrofitServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GarageRepo : IGarageRepo {
    override fun getGarageCategories(st: Int, callback: Repocallback<GarageCategoryResponseModel>) {

        val observable = RetrofitServiceBuilder.getApiService()
            .getGarageCategories(st)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(
            CallbackWrapper(object :
                CallbackWrapper.HttpHandler<GarageCategoryResponseModel>(callback) {
                override fun onSuccess(t: GarageCategoryResponseModel?) {
                    callback.onResult(
                        Resource(
                            t,
                            Resource.Status.SUCCESS,
                            ""
                        )
                    )

                }

                override fun onCreated(t: GarageCategoryResponseModel?) {

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

    override fun getSubGarageCategories(
        st: Int,
        cat: Int,
        callback: Repocallback<GarageSubCategoryResponseModel>
    ) {
        val observable = RetrofitServiceBuilder.getApiService()
            .getGarageSubCategories(st, cat)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(
            CallbackWrapper(object :
                CallbackWrapper.HttpHandler<GarageSubCategoryResponseModel>(callback) {
                override fun onSuccess(t: GarageSubCategoryResponseModel?) {
                    callback.onResult(
                        Resource(
                            t,
                            Resource.Status.SUCCESS,
                            ""
                        )
                    )

                }

                override fun onCreated(t: GarageSubCategoryResponseModel?) {

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

    override fun getGarageDetail(
        requestModel: GarageDetailRequestModel,
        callback: Repocallback<GarageDetailResponseModel>
    ) {

        val observable = RetrofitServiceBuilder.getApiService()
            .getGarageDetail(requestModel.st, requestModel.user_id, requestModel.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(CallbackWrapper(object :
                CallbackWrapper.HttpHandler<GarageDetailResponseModel>(callback) {
                override fun onSuccess(t: GarageDetailResponseModel?) {
                    callback.onResult(
                        Resource(
                            t,
                            Resource.Status.SUCCESS,
                            ""
                        )
                    )

                }

                override fun onCreated(t: GarageDetailResponseModel?) {

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

    override fun addGarage(
        requestModel: AddGarageRequestModel,
        callback: Repocallback<GlobalResponseModel>
    ) {
        val observable = RetrofitServiceBuilder.getApiService()
            .addGarage(
                API_ADD_GARGE,
                requestModel.user_id,
                requestModel.cat_id,
                requestModel.city_id,
                requestModel.name_ar,
                requestModel.name,
                requestModel.address,
                requestModel.email,
                requestModel.phone,
                requestModel.hours,
                requestModel.cover,
                requestModel.license
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(
            CallbackWrapper(object :
                CallbackWrapper.HttpHandler<GlobalResponseModel>(callback) {
                override fun onSuccess(t: GlobalResponseModel?) {
                    callback.onResult(
                        Resource(
                            t,
                            Resource.Status.SUCCESS,
                            ""
                        )
                    )

                }

                override fun onCreated(t: GlobalResponseModel?) {

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