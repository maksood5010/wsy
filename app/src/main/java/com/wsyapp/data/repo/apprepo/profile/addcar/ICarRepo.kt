package com.wsyapp.data.repo.apprepo.profile.addcar

import com.wsyapp.data.model.request.AddCarRequestModel
import com.wsyapp.data.model.request.CarDetailsRequestModel
import com.wsyapp.data.model.request.DeleteCarRequestModel
import com.wsyapp.data.model.request.EditCarRequestModel
import com.wsyapp.data.model.response.CarDetailsResponseModel
import com.wsyapp.data.model.response.CarTypeResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.repo_base.Repocallback

interface ICarRepo {
    fun addCar(requestModel: AddCarRequestModel, callBack: Repocallback<GlobalResponseModel>)
    fun editCar(requestModel: EditCarRequestModel, callBack: Repocallback<GlobalResponseModel>)
    fun getCarDetails(requestModel: CarDetailsRequestModel, callBack: Repocallback<CarDetailsResponseModel>)
    fun deleteCar(requestModel: DeleteCarRequestModel, callBack: Repocallback<GlobalResponseModel>)
    fun getCarType(st:Int, callBack: Repocallback<CarTypeResponseModel>)
}