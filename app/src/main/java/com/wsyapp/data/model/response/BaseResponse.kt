package com.wsyapp.data.model.response

open class BaseResponse {


    fun getValidString(data: String): String {
        return data ?: ""
    }

    protected fun getValidParagraphString(data: String): String {
        return data.replace(",,", "\n") ?: ""
    }
}
