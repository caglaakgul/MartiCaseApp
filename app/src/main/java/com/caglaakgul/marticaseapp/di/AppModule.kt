package com.caglaakgul.marticaseapp.di


import android.content.Context
import androidx.room.Room
import com.caglaakgul.marticaseapp.data.local.entity.LocationDatabase
import com.caglaakgul.marticaseapp.domain.repository.LocationRepository
import com.caglaakgul.marticaseapp.domain.repository.LocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocationRepository(@ApplicationContext context: Context, database: LocationDatabase): LocationRepository {
        return LocationRepositoryImpl(context, database)
    }

    @Provides
    @Singleton
    fun provideLocationDatabase(@ApplicationContext context: Context): LocationDatabase {
        return Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            "location_db"
        ).build()
    }
}
