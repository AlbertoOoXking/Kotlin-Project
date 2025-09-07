package com.albert.capstoneproject.local.orderDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.albert.capstoneproject.Data.model.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun update(item: Order)

    @Query("SELECT * FROM orderDatabase")
    fun getAllOrders(): LiveData<List<Order>>

    @Query("DELETE FROM orderDatabase")
    suspend fun deleteAll()

    @Query("DELETE FROM orderDatabase WHERE orderId = :id")
    suspend fun deleteById(id: Long)
}