package com.wsyapp.data.database.dao

import androidx.room.*
import com.wsyapp.data.database.entity.Cart

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun getAllItems(): MutableList<Cart>

    @Insert
    fun addItem(cart: Cart): Long

    @Delete
    fun deleteItem(cart: Cart): Int

    @Update
    fun updateItem(cart: Cart): Int
}