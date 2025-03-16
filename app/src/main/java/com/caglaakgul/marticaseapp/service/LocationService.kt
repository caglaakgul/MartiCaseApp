package com.caglaakgul.marticaseapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.caglaakgul.marticaseapp.domain.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var locationRepository: LocationRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        locationRepository.startTracking()
        return START_STICKY
    }

    override fun onDestroy() {
        locationRepository.stopTracking()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val channelId = "location_service_channel"
        val channel = NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_LOW).apply {
            description = "Arka planda konum izleme?"
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Konum Takibi")
            .setContentText("Uygulama arka planda çalışıyor ve konum kaydediliyor.")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
    }
}
