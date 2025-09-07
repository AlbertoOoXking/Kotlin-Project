package com.albert.capstoneproject.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// Converter class for handling LocalDateTime conversions in Room Database
class LocalDateTimeConverter {
    // DateTimeFormatter used for formatting LocalDateTime objects to Strings and vice versa
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Converts a LocalDateTime object to a String for storage in the database
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        // Use the formatter to convert the LocalDateTime to an ISO-8601 formatted string
        return localDateTime?.format(formatter)
    }

    // Converts a String back to a LocalDateTime object when reading from the database
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(localDateTimeString: String?): LocalDateTime? {
        // Parse the string using the formatter to convert it back to a LocalDateTime object
        return localDateTimeString?.let {
            LocalDateTime.parse(it, formatter)
        }
    }
}