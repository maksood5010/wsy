package com.wsyapp.data.repo.apprepo.login

import com.google.firebase.auth.PhoneAuthProvider
import com.wsyapp.data.model.request.VerifyRequestModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.model.response.ProfileResponseModel
import com.wsyapp.data.repo.RepoConstant.Companion.API_GET_USER_DETAILS
import com.wsyapp.data.repo.RepoConstant.Companion.API_VERIFY_UPDATE_USER
import com.wsyapp.data.repo.repo_base.CallbackWrapper
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import com.wsyapp.data.repo.repo_base.RetrofitServiceBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginRepo : ILoginRepo {

    override fun loginWithFirebase(
        phoneNumber: String,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {

    }

    override fun verifyUser(
        reqModel: VerifyRequestModel,
        callback: Repocallback<GlobalResponseModel>
    ) {
        val observable = RetrofitServiceBuilder.getApiService()
            .verifyUser(
                API_VERIFY_UPDATE_USER,
                reqModel.user_id,
                reqModel.email,
                reqModel.country_code_phone_number,
                reqModel.firebase,
                reqModel.device_id,
                reqModel.name,
                reqModel.address
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(
            CallbackWrapper(object : CallbackWrapper.HttpHandler<GlobalResponseModel>(callback) {
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

    override fun getUserProfile(user_id: String, callback: Repocallback<ProfileResponseModel>) {
        val observable = RetrofitServiceBuilder.getApiService()
            .getUserDetails(
                API_GET_USER_DETAILS,user_id
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribe(
            CallbackWrapper(object : CallbackWrapper.HttpHandler<ProfileResponseModel>(callback) {
                override fun onSuccess(t: ProfileResponseModel?) {
                    callback.onResult(
                        Resource(
                            t,
                            Resource.Status.SUCCESS,
                            ""
                        )
                    )

                }

                override fun onCreated(t: ProfileResponseModel?) {

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