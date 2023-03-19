package com.wsyapp.data.repo.apprepo.carpart

import com.wsyapp.data.model.response.CarPartDetailResponseModel
import com.wsyapp.data.model.response.CarPartResponseModel
import com.wsyapp.data.model.response.CartItemsResponseModel
import com.wsyapp.data.repo.repo_base.Repocallback

interface ICarPartRepo {
    fun getCarPartProducs(st: Int, callback: Repocallback<CarPartResponseModel>)
    fun getCarPartDetail(st: Int, id: String, callback: Repocallback<CarPartDetailResponseModel>)
    fun getCarItems(ids: String, callback: Repocallback<CartItemsResponseModel>)
}