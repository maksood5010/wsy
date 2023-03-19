package com.wsyapp.ui.home.carpart.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsyapp.data.database.AppDataBase
import com.wsyapp.data.database.entity.Cart
import com.wsyapp.data.model.response.CarPartDetailResponseModel
import com.wsyapp.data.repo.apprepo.carpart.CarPartRepo
import com.wsyapp.data.repo.repo_base.Repocallback
import com.wsyapp.data.repo.repo_base.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CarPartDetailViewModel : ViewModel() {
    private var dataBase: AppDataBase? = null

    private var liveData = MutableLiveData<Resource<CarPartDetailResponseModel, Action>>()
    private var addLiveData = MutableLiveData<Long>()
    private var cart_liveData = MutableLiveData<MutableList<Cart>>()
    var context: Context? = null

    enum class Action {
        SUCCESS, NOT_FOUND, SERVER_ERROR, NETWORK_ERROR
    }

    fun getLiveData(): LiveData<Resource<CarPartDetailResponseModel, Action>> {
        return liveData
    }

    fun addLiveData(): MutableLiveData<Long> {
        return addLiveData
    }

    fun getCartLiveData(): LiveData<MutableList<Cart>> {
        return cart_liveData
    }

    fun initDatabase(context: Context) {
        this.context = context
        dataBase = AppDataBase.getInstance(context)
    }

    fun getCarPartDetail(st: Int, id: String) {
        CarPartRepo().getCarPartDetail(st, id, object : Repocallback<CarPartDetailResponseModel> {
            override fun onResult(result: Resource<CarPartDetailResponseModel?, Resource.Status>) {
                when (result.action) {
                    Resource.Status.SUCCESS -> liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SUCCESS,
                            ""
                        )
                    )
                    Resource.Status.NOT_FOUND -> liveData.postValue(
                        Resource(
                            result.payload,
                            Action.NOT_FOUND,
                            ""
                        )
                    )
                    Resource.Status.SERVER_ERROR -> liveData.postValue(
                        Resource(
                            result.payload,
                            Action.SERVER_ERROR,
                            ""
                        )
                    )
                    else -> liveData.postValue(Resource(result.payload, Action.NETWORK_ERROR, ""))
                }
            }

        })
    }

    fun addInCart(cart: Cart) {
        Thread {
            val addItem = dataBase?.cartDao()?.addItem(cart)
            addLiveData.postValue(addItem ?: 0L)

        }.start()

    }

    fun getAllProducts() {
        Thread {
            val allItems = dataBase?.cartDao()?.getAllItems()
            cart_liveData.postValue(allItems)
        }.start()
    }

    fun deleteProduct(cart: Cart) {
        Thread {
            val delete = dataBase?.cartDao()?.deleteItem(cart)
        }.start()
    }

    fun getUpDateProduct(cart: Cart) {
        Thread {
            val update = dataBase?.cartDao()?.updateItem(cart)
            addLiveData.postValue(update?.toLong())
        }.start()
    }
}