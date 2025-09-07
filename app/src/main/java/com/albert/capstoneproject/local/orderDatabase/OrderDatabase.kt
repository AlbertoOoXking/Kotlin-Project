package com.albert.capstoneproject.local.orderDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.albert.capstoneproject.Data.model.Order
import com.albert.capstoneproject.local.Converter
import com.albert.capstoneproject.local.LocalDateTimeConverter


@Database(entities = [Order::class], version = 1)
@TypeConverters(Converter::class, LocalDateTimeConverter::class)
abstract class OrderDatabase : RoomDatabase() {
    abstract val dao: OrderDao

}

private lateinit var INSTANCE: OrderDatabase
fun getData(context: Context): OrderDatabase {

    synchronized(OrderDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, OrderDatabase::class.java, "orderDatabase"
            ).build()
        }
        return INSTANCE
    }
}