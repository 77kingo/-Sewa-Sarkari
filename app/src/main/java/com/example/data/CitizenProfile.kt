package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "citizen_profiles")
data class CitizenProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val dob: String,
    val citizenshipNumber: String,
    val district: String,
    val email: String,
    val phone: String
) : Serializable
