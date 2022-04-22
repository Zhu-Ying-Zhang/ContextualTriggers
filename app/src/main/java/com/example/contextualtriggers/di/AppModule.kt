package com.example.contextualtriggers.di

import android.app.Application
import androidx.room.Room
import com.example.contextualtriggers.context.room_database.Geofence.GeofenceDatabase
import com.example.contextualtriggers.context.room_database.Geofence.GeofenceRepoImplementation
import com.example.contextualtriggers.context.room_database.Geofence.GeofenceRepository
import com.example.contextualtriggers.context.room_database.Steps.StepsDatabase
import com.example.contextualtriggers.context.room_database.Steps.StepsRepoImplementation
import com.example.contextualtriggers.context.room_database.Steps.StepsRepository
import com.example.contextualtriggers.context.use_cases.Geofence.AddGeofence
import com.example.contextualtriggers.context.use_cases.Geofence.GeofenceUseCases
import com.example.contextualtriggers.context.use_cases.Geofence.GetGeofence
import com.example.contextualtriggers.context.use_cases.Steps.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideGeofenceDatabase(app: Application): GeofenceDatabase {
        return Room.databaseBuilder(
            app,
            GeofenceDatabase::class.java,
            GeofenceDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGeofenceRepository(db: GeofenceDatabase): GeofenceRepository {
        return GeofenceRepoImplementation(db.geofenceDao)
    }

    @Provides
    @Singleton
    fun provideGeofenceUseCases(repository: GeofenceRepository): GeofenceUseCases {
        return GeofenceUseCases(
            addGeofence = AddGeofence(repository),
            getGeofence = GetGeofence(repository)
        )
    }

    @Provides
    @Singleton
    fun provideStepsDatabase(app: Application): StepsDatabase {
        return Room.databaseBuilder(
            app,
            StepsDatabase::class.java,
            StepsDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideStepsRepository(db: StepsDatabase): StepsRepository {
        return StepsRepoImplementation(db.stepsDao)
    }

    @Provides
    @Singleton
    fun provideStepsUseCases(repository: StepsRepository): StepsUseCases {
        return StepsUseCases(
            addSteps = AddSteps(repository),
            getSteps = GetSteps(repository),
            insertSteps = InsertSteps(repository),
            stepsExist = StepsExist(repository),
        )
    }
}