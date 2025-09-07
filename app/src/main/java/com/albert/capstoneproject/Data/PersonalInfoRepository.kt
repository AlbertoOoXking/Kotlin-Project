package com.albert.capstoneproject.Data

import android.util.Log
import androidx.lifecycle.LiveData
import com.albert.capstoneproject.Data.model.PersonalInfo
import com.albert.capstoneproject.local.PersonalInfoDatabase.PersonalInfoDatabase

// Repository class for managing personal information
class PersonalInfoRepository(
    private val personalData: PersonalInfoDatabase // Local database instance for personal information
) {
    private val TAG = "PersonalInfoRepository" // Tag for logging

    // LiveData for observing personal information in the database
    val personalInformation: LiveData<List<PersonalInfo>> = personalData.personalDao.getInfo()

    // Function to retrieve personal information from the database
    fun getInfo() {
        try {
            // Retrieve personal information (currently unused result)
            personalData.personalDao.getInfo()
        } catch (e: Exception) {
            Log.e(TAG, "Error writing into database: $e")
        }
    }

    // Suspend function to insert new personal information into the database
    suspend fun insertInfo(info: PersonalInfo) {
        try {
            // Insert the personal information into the database
            personalData.personalDao.insertInfo(info)
        } catch (e: Exception) {
            Log.e(TAG, "Error writing into database: $e")
        }
    }

    // Suspend function to update existing personal information in the database
    suspend fun updateInfo(info: PersonalInfo) {
        try {
            // Update the personal information in the database
            personalData.personalDao.update(info)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }

    // Suspend function to delete all personal information from the database
    suspend fun deleteInfo(info: PersonalInfo) {
        try {
            // Delete all personal information from the database
            personalData.personalDao.deleteAll()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting from database: $e")
        }
    }
}