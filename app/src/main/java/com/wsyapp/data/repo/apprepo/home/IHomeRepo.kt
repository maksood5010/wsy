package com.wsyapp.data.repo.apprepo.home

import com.wsyapp.data.model.request.HomeRequestModel
import com.wsyapp.data.model.response.HomeSliderResponseModel
import com.wsyapp.data.repo.repo_base.Repocallback

interface IHomeRepo {
    fun gethomeSlider(
        requestModel: HomeRequestModel,
        callback: Repocallback<HomeSliderResponseModel>
    )
}