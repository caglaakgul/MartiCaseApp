package com.caglaakgul.marticaseapp.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.caglaakgul.marticaseapp.data.local.entity.LocationDatabase
import com.caglaakgul.marticaseapp.data.local.entity.LocationEntity
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationRepositoryImpl @Inject constructor(
    context: Context,
    database: LocationDatabase
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationFlow = MutableSharedFlow<Location>(replay = 1)
    private var lastLocation: Location? = null
    private val locationDao = database.locationDao()

    private val scope = CoroutineScope(Dispatchers.IO)

    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location> = locationFlow

    override fun getSavedLocations(): Flow<List<LocationEntity>> = locationDao.getAllLocations()

    @SuppressLint("MissingPermission")
    override fun startTracking() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateDistanceMeters(100f)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    if (lastLocation == null || lastLocation!!.distanceTo(location) >= 100) {
                        lastLocation = location
                        locationFlow.tryEmit(location)
                        scope.launch { saveLocation(location) }
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    override fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    override suspend fun saveLocation(location: Location) {
        val entity = LocationEntity(latitude = location.latitude, longitude = location.longitude)
        locationDao.insertLocation(entity)
    }

    override suspend fun clearAllLocations() {
        locationDao.clearAllLocations()
    }
}