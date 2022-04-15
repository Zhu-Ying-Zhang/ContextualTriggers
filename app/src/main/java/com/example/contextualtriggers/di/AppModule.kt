package com.example.contextualtriggers.di

import android.app.Application
import androidx.room.Room
import com.example.contextualtriggers.context.room_database.GeofenceDatabase
import com.example.contextualtriggers.context.room_database.GeofenceRepoImplementation
import com.example.contextualtriggers.context.room_database.GeofenceRepository
import com.example.contextualtriggers.context.use_cases.AddGeofence
import com.example.contextualtriggers.context.use_cases.GeofenceUseCases
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
    fun provideGoalRepository(db: GeofenceDatabase): GeofenceRepository {
        return GeofenceRepoImplementation(db.geofenceDao)
    }

    @Provides
    @Singleton
    fun provideGoalUseCases(repository: GeofenceRepository): GeofenceUseCases {
        return GeofenceUseCases(
            addGeofence = AddGeofence(repository),
//            deleteGoal = DeleteGoal(repository),
//            getGoal = GetGoal(repository),
//            getGoals = GetGoals(repository),
//            goalExists = GoalExists(repository),
//            setCurrentGoal = SetCurrentGoal(repository),
//            setActiveGoal = SetActiveGoal(repository)
        )
    }
}