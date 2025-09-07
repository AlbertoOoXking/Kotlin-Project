package com.albert.capstoneproject.local

import androidx.room.TypeConverter
import com.albert.capstoneproject.Data.model.PersonalInfo


// Converter class to handle custom data type conversions for PersonalInfo
class InfoConverter {

    // Converts a PersonalInfo object to a comma-separated string for storage in the database
    @TypeConverter
    fun fromPersonalInfo(info: PersonalInfo): String {
        // Create a comma-separated string from the properties of PersonalInfo
        return "${info.userName},${info.userLastName},${info.userCity},${info.userStreet},${info.userHouseNumber},${info.userPostalCode}"
    }

    // Converts a comma-separated string back to a PersonalInfo object when reading from the database
    @TypeConverter
    fun toPersonalInfo(data: String): PersonalInfo {
        // Split the comma-separated string into individual parts
        val parts = data.split(",")
        // Create and return a PersonalInfo object from the parts
        return PersonalInfo(
            userName = parts[0],
            userLastName = parts[1],
            userCity = parts[2],
            userStreet = parts[3],
            userHouseNumber = parts[4],
            userPostalCode = parts[5]
        )
    }
}