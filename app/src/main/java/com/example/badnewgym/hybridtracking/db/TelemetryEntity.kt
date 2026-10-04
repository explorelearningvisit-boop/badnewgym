package com.example.badnewgym.hybridtracking.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "telemetry_records")
data class TelemetryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val deviceId: String,
    val timestamp: Long,
    val payloadJson: String,
    val isSynced: Boolean = false,
    val retryCount: Int = 0
)
