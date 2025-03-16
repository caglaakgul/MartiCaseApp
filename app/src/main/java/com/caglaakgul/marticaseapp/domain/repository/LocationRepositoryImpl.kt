package com.caglaakgul.marticaseapp.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    context: Context
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationFlow = MutableSharedFlow<Location>(replay = 1)
    private var lastLocation: Location? = null

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location> = locationFlow

    @SuppressLint("MissingPermission")
    override fun startTracking() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateDistanceMeters(100f)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    if (lastLocation == null || lastLocation!!.distanceTo(location) >= 100) {
                        lastLocation = location
                        locationFlow.tryEmit(location)
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(object : LocationCallback() {})
    }
}