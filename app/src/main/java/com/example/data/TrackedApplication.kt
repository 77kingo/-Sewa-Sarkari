package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tracked_applications")
data class TrackedApplication(
    @PrimaryKey val trackingNumber: String,
    val serviceId: String,
    val applicantName: String,
    val serviceNameEn: String,
    val serviceNameNe: String,
    val statusTextEn: String,
    val statusTextNe: String,
    val progressPercent: Int,
    val lastChecked: String
) : Serializable
