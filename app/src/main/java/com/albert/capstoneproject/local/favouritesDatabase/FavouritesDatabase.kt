package com.albert.capstoneproject.local.favouritesDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.albert.capstoneproject.Data.model.Favourites

@Database(entities = [Favourites::class], version = 1)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract val favDau: FavouritesDau

}

private lateinit var INSTANCE: FavouritesDatabase
fun getAllData(context: Context): FavouritesDatabase {

    synchronized(FavouritesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, FavouritesDatabase::class.java, "favouritesDatabase"
            ).build()
        }
        return INSTANCE
    }
}