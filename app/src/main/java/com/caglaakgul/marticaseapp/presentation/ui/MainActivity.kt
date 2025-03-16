package com.caglaakgul.marticaseapp.presentation.ui


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.caglaakgul.marticaseapp.R
import com.caglaakgul.marticaseapp.databinding.ActivityMainBinding
import com.caglaakgul.marticaseapp.presentation.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private val viewModel: MapViewModel by viewModels()
    private var isTracking = false

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnStart.setOnClickListener {
            isTracking = true
            Toast.makeText(this, "Takip başlatıldı", Toast.LENGTH_SHORT).show()
            viewModel.startLocationUpdates()
        }

        binding.btnStop.setOnClickListener {
            isTracking = false
            Toast.makeText(this, "Takip durduruldu", Toast.LENGTH_SHORT).show()
            viewModel.stopLocationUpdates()
        }

        binding.btnReset.setOnClickListener {
            viewModel.clearSavedLocations()
            mMap.clear()
            Toast.makeText(this, "Tüm veriler sıfırlandı!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        enableMyLocation()
        observeLocationUpdates()

        mMap.setOnMarkerClickListener { marker ->
            val address = getAddress(marker.position)
            Toast.makeText(this, "Address: $address", Toast.LENGTH_SHORT).show()
            true
        }

        getLastKnownLocation()
    }

    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                }
            }
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
        }
    }

    private fun observeLocationUpdates() {
        lifecycleScope.launch {
            viewModel.locationFlow.collect { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                drawUserLocation(latLng)

                if (isTracking) {
                    addMarker(latLng)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.savedLocations.collect { locations ->
                locations.forEach { loc ->
                    val latLng = LatLng(loc.latitude, loc.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng))
                }
            }
        }
    }

    private fun drawUserLocation(latLng: LatLng) {
        val circleOptions = CircleOptions()
            .center(latLng)
            .radius(10.0)
            .strokeColor(Color.BLUE)
            .fillColor(Color.argb(70, 50, 50, 255))
        mMap.addCircle(circleOptions)
    }

    private fun addMarker(latLng: LatLng) {
        mMap.addMarker(MarkerOptions().position(latLng))
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                ?.firstOrNull()
                ?.getAddressLine(0)
                ?: "Address not found!"
        } catch (e: Exception) {
            "Address not available"
        }
    }
}
