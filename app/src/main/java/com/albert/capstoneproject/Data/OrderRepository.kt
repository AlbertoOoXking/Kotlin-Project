package com.albert.capstoneproject.Data

import android.util.Log
import androidx.lifecycle.LiveData
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Order
import com.albert.capstoneproject.local.orderDatabase.OrderDatabase

// Repository class for managing orders
class OrderRepository(
    private val data: OrderDatabase, // Local database instance for orders
) {
    private val TAG = "OrderRepository" // Tag for logging

    // LiveData for observing the list of orders in the database
    val orderList: LiveData<List<Order>> = data.dao.getAllOrders()

    // Suspend function to insert a new order into the database
    suspend fun insertOrder(order: Order) {
        try {
            // Insert the order into the database
            data.dao.insertOrder(order)
        } catch (e: Exception) {
            Log.e(TAG, "Error writing into database: $e")
        }
    }

    // Suspend function to update an existing order in the database
    suspend fun update(orderItem: Order) {
        try {
            // Update the order in the database
            data.dao.update(orderItem)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }

    // Suspend function to delete an order from the database
    suspend fun delete(orderItem: Order) {
        try {
            // Delete the order from the database by its ID
            data.dao.deleteById(orderItem.orderId)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting from database: $e")
        }
    }
}