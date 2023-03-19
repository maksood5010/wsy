package com.wsyapp.data.repo.apprepo.cart

import com.wsyapp.data.model.request.PlaceOrderRequestModel
import com.wsyapp.data.model.response.PlaceOrderResponseModel
import com.wsyapp.data.repo.repo_base.CallbackWrapper
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import com.wsyapp.data.repo.repo_base.RetrofitServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PlaceOrderRepo : IPlaceOrderRepo {
    override fun placeOrder(
        placeOrderRequestModel: PlaceOrderRequestModel,
        callback: Repocallback<PlaceOrderResponseModel>
    ) {
        val observable = RetrofitServiceBuilder.getApiService()
            .placeOrder(
                placeOrderRequestModel.st,
                placeOrderRequestModel.user_id,
                placeOrderRequestModel.phone,
                placeOrderRequestModel.ddate,
                placeOrderRequestModel.payment,
                placeOrderRequestModel.delivery,
                placeOrderRequestModel.total,
                placeOrderRequestModel.device,
                placeOrderRequestModel.orders,
                placeOrderRequestModel.address
            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(
            CallbackWrapper(object : CallbackWrapper.HttpHandler<PlaceOrderResponseModel>(callback) {
                override fun onSuccess(t: PlaceOrderResponseModel?) {
                    callback.onResult(
                        Resource(
                            t,
                            Resource.Status.SUCCESS,
                            ""
                        )
                    )

                }

                override fun onCreated(t: PlaceOrderResponseModel?) {

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