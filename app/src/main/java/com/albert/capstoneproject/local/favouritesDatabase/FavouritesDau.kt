package com.albert.capstoneproject.local.favouritesDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Favourites

@Dao
interface FavouritesDau {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(fav: Favourites)

    @Update
    suspend fun update(item: Favourites)

    @Query("SELECT * FROM favouritesDatabase")
    fun getAllFav(): LiveData<List<Favourites>>

    @Query("DELETE FROM favouritesDatabase")
    suspend fun deleteAll()

    @Query("DELETE FROM favouritesDatabase WHERE favouriteId = :id")
    suspend fun deleteById(id: Long)

}