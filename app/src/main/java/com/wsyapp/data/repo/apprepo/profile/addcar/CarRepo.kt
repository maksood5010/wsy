package com.wsyapp.data.repo.apprepo.profile.addcar

import com.wsyapp.data.model.request.AddCarRequestModel
import com.wsyapp.data.model.request.CarDetailsRequestModel
import com.wsyapp.data.model.request.DeleteCarRequestModel
import com.wsyapp.data.model.request.EditCarRequestModel
import com.wsyapp.data.model.response.CarDetailsResponseModel
import com.wsyapp.data.model.response.CarTypeResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.repo_base.CallbackWrapper
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import com.wsyapp.data.repo.repo_base.RetrofitServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CarRepo : ICarRepo {
    override fun addCar(
        requestModel: AddCarRequestModel,
        callBack: Repocallback<GlobalResponseModel>
    ) {
        val observeOn = RetrofitServiceBuilder.getApiService().addCar(
            requestModel.st,
            requestModel.name,
            requestModel.user_id,
            requestModel.model,
            requestModel.plate,
            requestModel.image,
            requestModel.car, requestModel.type
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<GlobalResponseModel>(callBack) {
            override fun onSuccess(t: GlobalResponseModel?) {
                callBack.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onCreated(t: GlobalResponseModel?) {

            }

            override fun onServerError() {
                callBack.onResult(Resource(null, Resource.Status.SERVER_ERROR, ""))
                super.onServerError()
            }


        }))
    }

    override fun editCar(
        requestModel: EditCarRequestModel,
        callBack: Repocallback<GlobalResponseModel>
    ) {
        val observeOn = RetrofitServiceBuilder.getApiService().editCar(
            requestModel.st,
            requestModel.name,
            requestModel.user_id,
            requestModel.model,
            requestModel.plate,
            requestModel.id, requestModel.image,
            requestModel.car
            , requestModel.type
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<GlobalResponseModel>(callBack) {
            override fun onCreated(t: GlobalResponseModel?) {
            }

            override fun onSuccess(t: GlobalResponseModel?) {
                callBack.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onServerError() {
                callBack.onResult(Resource(null, Resource.Status.SUCCESS, ""))
                super.onServerError()
            }

        }))
    }

    override fun getCarDetails(
        requestModel: CarDetailsRequestModel,
        callBack: Repocallback<CarDetailsResponseModel>
    ) {
        val observeOn = RetrofitServiceBuilder.getApiService()
            .getCarDetails(requestModel.st, requestModel.user_id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<CarDetailsResponseModel>(callBack) {
            override fun onCreated(t: CarDetailsResponseModel?) {
            }

            override fun onSuccess(t: CarDetailsResponseModel?) {
                callBack.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onServerError() {
                callBack.onResult(Resource(null, Resource.Status.SUCCESS, ""))
                super.onServerError()
            }

        }))
    }

    override fun deleteCar(
        requestModel: DeleteCarRequestModel,
        callBack: Repocallback<GlobalResponseModel>
    ) {
        val observeOn = RetrofitServiceBuilder.getApiService()
            .deleteCar(requestModel.st, requestModel.user_id, requestModel.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<GlobalResponseModel>(callBack) {
            override fun onCreated(t: GlobalResponseModel?) {
            }

            override fun onSuccess(t: GlobalResponseModel?) {
                callBack.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onServerError() {
                callBack.onResult(Resource(null, Resource.Status.SUCCESS, ""))
                super.onServerError()
            }

        }))
    }

    override fun getCarType(st: Int, callBack: Repocallback<CarTypeResponseModel>) {
        val observeOn = RetrofitServiceBuilder.getApiService()
            .getCarType(st)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observeOn.subscribe(CallbackWrapper(object :
            CallbackWrapper.HttpHandler<CarTypeResponseModel>(callBack) {
            override fun onCreated(t: CarTypeResponseModel?) {
            }

            override fun onSuccess(t: CarTypeResponseModel?) {
                callBack.onResult(Resource(t, Resource.Status.SUCCESS, ""))
            }

            override fun onServerError() {
                callBack.onResult(Resource(null, Resource.Status.SUCCESS, ""))
                super.onServerError()
            }

        }))
    }


}