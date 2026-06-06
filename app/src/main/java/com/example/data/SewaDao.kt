package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SewaDao {
    // Citizen Profile operations
    @Query("SELECT * FROM citizen_profiles LIMIT 1")
    fun getPrimaryProfile(): Flow<CitizenProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: CitizenProfile)

    @Query("DELETE FROM citizen_profiles")
    suspend fun clearProfiles()

    // Tracked Applications operations
    @Query("SELECT * FROM tracked_applications ORDER BY lastChecked DESC")
    fun getAllTracked(): Flow<List<TrackedApplication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracked(app: TrackedApplication)

    @Query("DELETE FROM tracked_applications WHERE trackingNumber = :trackingNumber")
    suspend fun deleteTrackedByNumber(trackingNumber: String)

    @Query("SELECT * FROM tracked_applications WHERE trackingNumber = :trackingNumber")
    suspend fun getTrackedByNumber(trackingNumber: String): TrackedApplication?
}
