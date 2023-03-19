package com.wsyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.wsyapp.data.model.request.VerifyRequestModel

class UserPref(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val PREF_USER = "user_data"
    private val KEY_UEER = "user"


    init {

        sharedPreferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
    }

    fun saveUserData(userModle: VerifyRequestModel) {
        val toJson = Gson().toJson(userModle)
        val edit = sharedPreferences.edit()
        edit.putString(KEY_UEER, toJson)
        edit.apply()
    }

    fun getUserModel(): VerifyRequestModel? {
        val string = sharedPreferences.getString(KEY_UEER, null)
        val model = Gson().fromJson(string, VerifyRequestModel::class.java)
        return model
    }

}