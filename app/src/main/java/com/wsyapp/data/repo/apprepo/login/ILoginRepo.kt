package com.wsyapp.data.repo.apprepo.login

import com.google.firebase.auth.PhoneAuthProvider
import com.wsyapp.data.model.request.VerifyRequestModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.model.response.ProfileResponseModel
import com.wsyapp.data.repo.repo_base.Repocallback

interface ILoginRepo {
    fun loginWithFirebase(
        phoneNumber: String,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    fun verifyUser(veifyRequestModel: VerifyRequestModel, callback: Repocallback<GlobalResponseModel>)
    fun getUserProfile(user_id:String, callback: Repocallback<ProfileResponseModel>)

}