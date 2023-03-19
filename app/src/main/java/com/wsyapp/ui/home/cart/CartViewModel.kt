package com.wsyapp.ui.home.cart

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.database.AppDataBase
import com.wsyapp.data.database.entity.Cart
import com.wsyapp.data.model.request.HomeRequestModel
import com.wsyapp.data.model.response.CartItemsResponseModel
import com.wsyapp.data.model.response.HomeSliderResponseModel
import com.wsyapp.data.repo.apprepo.carpart.CarPartRepo
import com.wsyapp.data.repo.apprepo.home.HomeRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource

class CartViewModel() : ViewModel() {
    private var liveData = MutableLiveData<MutableList<Cart>>()
    private var dataBase: AppDataBase? = null
    var context: Context? = null

    private var cartResponse_liveData = MutableLiveData<Resource<CartItemsResponseModel, Action>>()

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun initDatabase(context: Context) {
        this.context = context
        dataBase = AppDataBase.getInstance(context)
    }

    fun getLiveData(): LiveData<MutableList<Cart>> {
        return liveData
    }

    fun getCartResponseLiveData(): MutableLiveData<Resource<CartItemsResponseModel, Action>> {
        return cartResponse_liveData
    }

    fun getAllProducts() {
        Thread {
            val allItems = dataBase?.cartDao()?.getAllItems()
            if (allItems != null) {
                Log.d("TAG", "getAllProducts: size : ${allItems.size}")
            }
            liveData.postValue(allItems)
        }.start()
    }

    fun getUpDateProduct(cart: Cart) {
        Thread {

            val update = dataBase?.cartDao()?.updateItem(cart)
            // getAllProducts()
        }.start()
    }

    fun deleteProduct(cart: Cart) {
        Thread {
            val delete = dataBase?.cartDao()?.deleteItem(cart)
            //  getAllProducts()
        }.start()
    }

    fun getCartItems(ids: String) {
        CarPartRepo().getCarItems(ids, object : Repocallback<CartItemsResponseModel> {
            override fun onResult(result: Resource<CartItemsResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> cartResponse_liveData.postValue(
                        Resource(
                            result.payload,
                            CartViewModel.Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> cartResponse_liveData.postValue(
                        Resource(
                            result.payload,
                            CartViewModel.Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> cartResponse_liveData.postValue(
                        Resource(
                            result.payload,
                            CartViewModel.Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> cartResponse_liveData.postValue(
                        Resource(
                            result.payload,
                            CartViewModel.Action.NETWORK_ERROR,
                            ""
                        )
                    )
                }
            }
        })
    }
}