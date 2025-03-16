package com.caglaakgul.marticaseapp.domain.repository

import android.location.Location
import com.caglaakgul.marticaseapp.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<Location>
    fun getSavedLocations(): Flow<List<LocationEntity>>
    fun startTracking()
    fun stopTracking()
    suspend fun saveLocation(location: Location)
    suspend fun clearAllLocations()
}