package com.albert.capstoneproject.local.PersonalInfoDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.albert.capstoneproject.Data.model.PersonalInfo
import com.albert.capstoneproject.local.InfoConverter

@Database(entities = [PersonalInfo::class], version = 1)
@TypeConverters(InfoConverter::class)
abstract class PersonalInfoDatabase : RoomDatabase() {
    abstract val personalDao: PersonalDao

}
private lateinit var INSTANCE: PersonalInfoDatabase
fun getAllInfo(context: Context): PersonalInfoDatabase {

    synchronized(PersonalInfoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                PersonalInfoDatabase::class.java,
                "personalDatabase"
            ).build()
        }
        return INSTANCE
    }
}