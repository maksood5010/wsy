package com.wsyapp.data.repo.apprepo.garage

import com.wsyapp.data.model.request.AddGarageRequestModel
import com.wsyapp.data.model.request.GarageDetailRequestModel
import com.wsyapp.data.model.response.GarageCategoryResponseModel
import com.wsyapp.data.model.response.GarageDetailResponseModel
import com.wsyapp.data.model.response.GarageSubCategoryResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.repo_base.Repocallback

interface IGarageRepo {
    fun getGarageCategories(st: Int, callback: Repocallback<GarageCategoryResponseModel>)
    fun getSubGarageCategories(st: Int, cat: Int, callback: Repocallback<GarageSubCategoryResponseModel>)
    fun getGarageDetail(requestModel: GarageDetailRequestModel, callback: Repocallback<GarageDetailResponseModel>)
    fun addGarage(requestModel: AddGarageRequestModel, callback: Repocallback<GlobalResponseModel>)
}