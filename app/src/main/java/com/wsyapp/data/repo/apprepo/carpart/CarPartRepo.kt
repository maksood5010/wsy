package com.wsyapp.data.repo.apprepo.carpart

import com.wsyapp.data.model.response.CarPartDetailResponseModel
import com.wsyapp.data.model.response.CarPartResponseModel
import com.wsyapp.data.model.response.CartItemsResponseModel
import com.wsyapp.data.repo.RepoConstant.Companion.API_GET_CART_ITEMS
import com.wsyapp.data.repo.repo_base.CallbackWrapper
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import com.wsyapp.data.repo.repo_base.RetrofitServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CarPartRepo : ICarPartRepo {
    override fun getCarPartProducs(st: Int, callback: Repocallback<CarPartResponseModel>) {
        val observalble =
            RetrofitServiceBuilder.getApiService().getCarPartProducs(st)
        val observeOn =
            observalble.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<CarPartResponseModel>(callback) {
            override fun onCreated(t: CarPartResponseModel?) {

            }

            override fun onSuccess(t: CarPartResponseModel?) {
                callback.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onServerError() {
                callback.onResult(Resource(null, Resource.Status.SERVER_ERROR, ""))

                super.onServerError()
            }

        }))
    }

    override fun getCarPartDetail(
        st: Int,
        id: String,
        callback: Repocallback<CarPartDetailResponseModel>
    ) {
        val observalble =
            RetrofitServiceBuilder.getApiService().getCarPartDetail(st, id)
        val observeOn =
            observalble.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<CarPartDetailResponseModel>(callback) {
            override fun onCreated(t: CarPartDetailResponseModel?) {

            }

            override fun onSuccess(t: CarPartDetailResponseModel?) {
                callback.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onServerError() {
                callback.onResult(Resource(null, Resource.Status.SERVER_ERROR, ""))

                super.onServerError()
            }

        }))
    }

    override fun getCarItems(ids: String, callback: Repocallback<CartItemsResponseModel>) {
        val observalble =
            RetrofitServiceBuilder.getApiService().getCartItems(API_GET_CART_ITEMS, ids)
        val observeOn =
            observalble.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<CartItemsResponseModel>(callback) {
            override fun onCreated(t: CartItemsResponseModel?) {

            }

            override fun onSuccess(t: CartItemsResponseModel?) {
                callback.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onServerError() {
                callback.onResult(Resource(null, Resource.Status.SERVER_ERROR, ""))

                super.onServerError()
            }

        }))
    }
}