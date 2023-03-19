package com.wsyapp.data.model.response

data class PolicyResponseModel(
    val `data`: List<FaqModel>,
    val success: Boolean
)

data class FaqModel(
    val details: String,
    val title: String
) : BaseResponse() {

    fun getParagraphText(): String {

        return getValidParagraphString(details)
    }

}