package com.example.badnewgym.hybridtracking.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TelemetryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(record: TelemetryEntity): Long

    @Query("SELECT * FROM telemetry_records WHERE isSynced = 0 ORDER BY timestamp ASC LIMIT :limit")
    fun getPendingRecords(limit: Int = 50): List<TelemetryEntity>

    @Query("SELECT COUNT(*) FROM telemetry_records WHERE isSynced = 0")
    fun getPendingCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM telemetry_records WHERE isSynced = 0")
    fun getPendingCount(): Int

    @Query("DELETE FROM telemetry_records WHERE id IN (:ids)")
    fun deleteRecords(ids: List<Long>): Int

    @Update
    fun updateRecords(records: List<TelemetryEntity>): Int

    @Query("DELETE FROM telemetry_records WHERE timestamp < :cutoffTimestamp")
    fun purgeOldSyncedRecords(cutoffTimestamp: Long): Int
}
