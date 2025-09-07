package com.albert.capstoneproject.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.albert.capstoneproject.Data.model.CartItems
//import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.Data.remote.ApiService
import com.albert.capstoneproject.local.cartDatabase.CartDatabase


// Repository class for managing cart and product data
class CartRepository(
    private val apiService: ApiService.ShopApi, // Retrofit service for API calls
    private val database: CartDatabase, // Local database instance
) {
    private val TAG = "CartRepository" // Tag for logging

    // LiveData for observing cart items in the database
    val cartItems: LiveData<List<CartItems>> = database.dao.getAll()

    // LiveData for observing products fetched from the API
    private var _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>>
        get() = _allProducts

    private var itemCount = 1 // Local variable for item count

    // Function to update the item count
    fun updateItemCount(count: Int) {
        itemCount = count
    }

    // Suspend function to fetch products from the API
    suspend fun getProducts() {
        try {
            // Make API call to fetch products
            val result = apiService.retrofitService.loadProducts()
            // Update LiveData with the fetched products
            _allProducts.postValue(result)
        } catch (e: Exception) {
            Log.e("REPOSITORY", "getRandomProducts: $e")
        }
    }

    // Suspend function to insert a cart item into the database
    suspend fun insert(item: CartItems) {
        try {
            // Insert item into the database
            database.dao.insert(item)
        } catch (e: Exception) {
            Log.e(TAG, "Error writing into database: $e")
        }
    }

    // Suspend function to update a cart item in the database
    suspend fun update(item: CartItems) {
        try {
            // Update item in the database
            database.dao.update(item)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }

    // Suspend function to delete a cart item from the database
    suspend fun delete(item: CartItems) {
        try {
            // Delete item from the database by its ID
            database.dao.deleteById(item.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting from database: $e")
        }
    }
}