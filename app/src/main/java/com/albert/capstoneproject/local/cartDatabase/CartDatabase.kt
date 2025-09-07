package com.albert.capstoneproject.local.cartDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.local.Converter


@Database(entities = [CartItems::class], version = 1)
@TypeConverters(Converter::class)
abstract class CartDatabase : RoomDatabase() {
    abstract val dao: CartDau

}

private lateinit var INSTANCE: CartDatabase
fun getDatabase(context: Context): CartDatabase {

    synchronized(CartDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, CartDatabase::class.java, "cartDatabase"
            ).build()
        }
        return INSTANCE
    }
}