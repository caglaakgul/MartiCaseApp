package com.caglaakgul.marticaseapp.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caglaakgul.marticaseapp.data.local.dao.LocationDao

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}