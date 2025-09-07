package com.albert.capstoneproject.local

import androidx.room.TypeConverter
import com.albert.capstoneproject.Data.model.CartItems
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


// Converter class to handle custom data type conversions
class Converter {

    // Converts a List<CartItems> to a JSON string for storage in the database
    @TypeConverter
    fun fromCartItemsList(value: List<CartItems>): String {
        // Create a Gson instance to handle JSON conversion
        val gson = Gson()
        // Define the type for Gson to use
        val type = object : TypeToken<List<CartItems>>() {}.type
        // Convert the List<CartItems> to JSON string
        return gson.toJson(value, type)
    }

    // Converts a JSON string back to a List<CartItems> when reading from the database
    @TypeConverter
    fun toCartItemsList(value: String): List<CartItems> {
        // Create a Gson instance to handle JSON conversion
        val gson = Gson()
        // Define the type for Gson to use
        val type = object : TypeToken<List<CartItems>>() {}.type
        // Convert the JSON string to a List<CartItems>
        return gson.fromJson(value, type)
    }

}