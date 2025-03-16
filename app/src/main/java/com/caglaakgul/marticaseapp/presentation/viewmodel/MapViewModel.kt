package com.caglaakgul.marticaseapp.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caglaakgul.marticaseapp.data.local.entity.LocationEntity
import com.caglaakgul.marticaseapp.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    val locationFlow: Flow<Location> = locationRepository.getLocationUpdates()
    val savedLocations: Flow<List<LocationEntity>> = locationRepository.getSavedLocations()

    fun startLocationUpdates() {
        locationRepository.startTracking()
    }

    fun stopLocationUpdates() {
        locationRepository.stopTracking()
    }

    fun clearSavedLocations() {
        viewModelScope.launch {
            locationRepository.clearAllLocations()
        }
    }
}