package com.albert.capstoneproject.Data

import android.util.Log
import androidx.lifecycle.LiveData
import com.albert.capstoneproject.Data.model.CartItems
import com.albert.capstoneproject.Data.model.Favourites
import com.albert.capstoneproject.Data.model.Product
import com.albert.capstoneproject.local.favouritesDatabase.FavouritesDatabase

// Repository class for managing favorite items
class FavouritesRepository(
    private val fav: FavouritesDatabase, // Local database instance
) {
    private val TAG = "FavouritesRepository" // Tag for logging

    // LiveData for observing favorite items in the database
    val favList: LiveData<List<Favourites>> = fav.favDau.getAllFav()

    // Suspend function to insert a favorite item into the database
    suspend fun insertFavourites(favItem: Favourites) {
        try {
            // Insert the favorite item into the database
            fav.favDau.insertOrder(favItem)
        } catch (e: Exception) {
            Log.e(TAG, "Error writing into database: $e")
        }
    }

    // Suspend function to update a favorite item in the database
    suspend fun update(favItem: Favourites) {
        try {
            // Update the favorite item in the database
            fav.favDau.update(favItem)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }

    // Suspend function to delete a favorite item from the database
    suspend fun delete(favItem: Favourites) {
        try {
            // Delete the favorite item from the database by its ID
            fav.favDau.deleteById(favItem.favouriteId)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting from database: $e")
        }
    }
}
