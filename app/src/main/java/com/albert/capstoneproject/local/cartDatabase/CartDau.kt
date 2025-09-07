package com.albert.capstoneproject.local.cartDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.albert.capstoneproject.Data.model.CartItems

@Dao
interface CartDau {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItems)

    @Update
    suspend fun update(item: CartItems)

    @Query("SELECT * FROM cartDatabase")
    fun getAll(): LiveData<List<CartItems>>

    @Query("DELETE FROM cartDatabase")
    suspend fun deleteAll()

    @Query("DELETE FROM cartDatabase WHERE id = :id")
    suspend fun deleteById(id: Long)
}