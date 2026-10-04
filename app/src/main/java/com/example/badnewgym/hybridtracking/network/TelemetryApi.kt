package com.example.badnewgym.hybridtracking.network

import com.example.badnewgym.hybridtracking.model.LocationResult
import com.example.badnewgym.hybridtracking.model.TelemetryPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class IngestionResponse(
    val status: String,
    val received_count: Int,
    val location: LocationResult? = null
)

interface TelemetryApi {
    @POST("api/v1/telemetry/report")
    suspend fun sendTelemetryReport(@Body payload: TelemetryPayload): Response<IngestionResponse>

    @POST("api/v1/telemetry/batch")
    suspend fun sendTelemetryBatch(@Body payloads: List<TelemetryPayload>): Response<IngestionResponse>

    @GET("api/v1/telemetry/location/{device_id}")
    suspend fun getResolvedLocation(@Path("device_id") deviceId: String): Response<LocationResult>
}
