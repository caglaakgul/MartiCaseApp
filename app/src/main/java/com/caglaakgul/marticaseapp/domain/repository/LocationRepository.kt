package com.caglaakgul.marticaseapp.domain.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<Location>
    fun startTracking()
    fun stopTracking()
}