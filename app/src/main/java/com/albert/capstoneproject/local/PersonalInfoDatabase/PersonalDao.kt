package com.albert.capstoneproject.local.PersonalInfoDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.albert.capstoneproject.Data.model.PersonalInfo

@Dao
interface PersonalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfo(info: PersonalInfo)

    @Update
    suspend fun update(info: PersonalInfo)

    @Query("SELECT * FROM personalDatabase")
    fun getInfo(): LiveData<List<PersonalInfo>>

    @Query("DELETE FROM personalDatabase")
    suspend fun deleteAll()
}