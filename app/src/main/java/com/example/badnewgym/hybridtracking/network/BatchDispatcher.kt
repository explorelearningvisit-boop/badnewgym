package com.example.badnewgym.hybridtracking.network

import android.content.Context
import com.example.badnewgym.hybridtracking.db.TelemetryDatabase
import com.example.badnewgym.hybridtracking.model.LocationResult
import com.example.badnewgym.hybridtracking.model.TelemetryPayload
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BatchDispatcher(private val context: Context, baseUrl: String = "http://127.0.0.1:8000/") {
    private val db = TelemetryDatabase.getInstance(context)
    private val gson = Gson()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val api: TelemetryApi by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TelemetryApi::class.java)
    }

    suspend fun flushPendingTelemetry(): Result<LocationResult?> = withContext(Dispatchers.IO) {
        val pendingEntities = db.telemetryDao().getPendingRecords(limit = 30)
        if (pendingEntities.isEmpty()) {
            return@withContext Result.success(null)
        }

        val payloads = pendingEntities.mapNotNull { entity ->
            try {
                gson.fromJson(entity.payloadJson, TelemetryPayload::class.java)
            } catch (e: Exception) {
                null
            }
        }

        if (payloads.isEmpty()) {
            db.telemetryDao().deleteRecords(pendingEntities.map { it.id })
            return@withContext Result.success(null)
        }

        var attempt = 0
        val maxAttempts = 3
        var backoffMs = 1000L
        var latestLocation: LocationResult? = null

        while (attempt < maxAttempts) {
            try {
                val response = if (payloads.size == 1) {
                    api.sendTelemetryReport(payloads.first())
                } else {
                    api.sendTelemetryBatch(payloads)
                }

                if (response.isSuccessful) {
                    val body = response.body()
                    latestLocation = body?.location
                    // Clean up synced records from Room
                    db.telemetryDao().deleteRecords(pendingEntities.map { it.id })
                    return@withContext Result.success(latestLocation)
                } else {
                    attempt++
                    delay(backoffMs)
                    backoffMs *= 2
                }
            } catch (e: Exception) {
                attempt++
                if (attempt >= maxAttempts) {
                    // Update retry counts in DB
                    val updated = pendingEntities.map { it.copy(retryCount = it.retryCount + 1) }
                    db.telemetryDao().updateRecords(updated)
                    return@withContext Result.failure(e)
                }
                delay(backoffMs)
                backoffMs *= 2
            }
        }

        Result.failure(Exception("Failed to dispatch telemetry after $maxAttempts attempts"))
    }
}
