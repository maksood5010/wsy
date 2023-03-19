package com.wsyapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wsyapp.data.database.dao.CartDao
import com.wsyapp.data.database.entity.Cart

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    companion object {
        fun getInstance(context: Context): AppDataBase {

            return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "my_db"
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).fallbackToDestructiveMigration().build()
        }
    }
}