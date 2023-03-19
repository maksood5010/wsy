package com.wsyapp.data.repo

import com.wsyapp.data.model.request.OrderRequestModel
import com.wsyapp.data.model.response.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {


    @FormUrlEncoded
    @POST("index.php")
    fun getFaq(
        @Field("st") st: Int,
        @Field("data") data: Int,
        @Field("lg") lg: String
    ): Observable<Response<PolicyResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getHomeSlider(
        @Field("st") st: Int,
        @Field("version") version: Int,
        @Field("user_id") user_id: Int
    ): Observable<Response<HomeSliderResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun addCar(
        @Field("st") st: Int,
        @Field("name") name: String,
        @Field("user_id") user_id: String,
        @Field("model") model: String,
        @Field("plate") plate: String,
        @Field("image") image: String,
        @Field("car") car: String,
        @Field("type") type: String
    ): Observable<Response<GlobalResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun editCar(
        @Field("st") st: Int,
        @Field("name") name: String,
        @Field("user_id") user_id: String,
        @Field("model") model: String,
        @Field("plate") plate: String,
        @Field("id") car_id: String,
        @Field("image") image: String,
        @Field("car") car: String,
        @Field("type") type: String
    ): Observable<Response<GlobalResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getCarDetails(
        @Field("st") st: Int,
        @Field("user_id") user_id: String
    ): Observable<Response<CarDetailsResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun deleteCar(
        @Field("st") st: Int,
        @Field("user_id") user_id: String,
        @Field("id") id: String
    ): Observable<Response<GlobalResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getGarageCategories(
        @Field("st") st: Int
    ): Observable<Response<GarageCategoryResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getGarageSubCategories(
        @Field("st") st: Int,
        @Field("cat") cat: Int
    ): Observable<Response<GarageSubCategoryResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getGarageDetail(
        @Field("st") st: Int,
        @Field("user_id") user_id: String,
        @Field("id") id: String
    ): Observable<Response<GarageDetailResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getCarType(
        @Field("st") st: Int
    ): Observable<Response<CarTypeResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun verifyUser(
        @Field("st") st: Int,
        @Field("user_id") user_id: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("fire") fire: String,
        @Field("device") device: Int,
        @Field("name") name: String,
        @Field("address") address: String
    ): Observable<Response<GlobalResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun addGarage(
        @Field("st") st: Int,
        @Field("user_id") user_id: String,
        @Field("cat_id") cat_id: String,
        @Field("city_id") city_id: String,
        @Field("name_ar") name_ar: String,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("hours") hours: String,
        @Field("cover") cover: String,
        @Field("license") license: String
    ): Observable<Response<GlobalResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getCarPartProducs(
        @Field("st") st: Int
    ): Observable<Response<CarPartResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getCarPartDetail(
        @Field("st") st: Int,
        @Field("id") id: String
    ): Observable<Response<CarPartDetailResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getUserDetails(
        @Field("st") st: Int,
        @Field("user_id") user_id: String
    ): Observable<Response<ProfileResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun getCartItems(
        @Field("st") st: Int,
        @Field("id") user_id: String
    ): Observable<Response<CartItemsResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun putAddress(
        @Field("st") st: Int,
        @Field("User_id") User_id: String,
        @Field("phone") phone: String,
        @Field("area") area: String,
        @Field("street") street: String,
        @Field("building") building: String,
        @Field("appartment") appartment: String,
        @Field("floor") floor: String,
        @Field("lat") lat: String,
        @Field("lon") lon: String
    ): Observable<Response<GlobalResponseModel>>

    @FormUrlEncoded
    @POST("index.php")
    fun placeOrder(
        @Field("st") st: Int,
        @Field("user_id") user_id: String,
        @Field("phone") phone: String,
        @Field("ddate") ddate: String,
        @Field("payment") payment: Int,
        @Field("delivery") delivery: String,
        @Field("total") total: String,
        @Field("device") device: String,
        @Field("orders") orders: MutableList<OrderRequestModel>,
        @Field("address") address: MutableList<AddressForPlaceOrderModel>?
    ): Observable<Response<PlaceOrderResponseModel>>

}