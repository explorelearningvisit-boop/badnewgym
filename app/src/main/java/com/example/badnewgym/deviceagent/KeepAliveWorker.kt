package com.example.badnewgym.deviceagent

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class KeepAliveWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.i(TAG, "KeepAliveWorker watchdog triggered")
        try {
            BackgroundKeepAliveManager.startAllBackgroundServices(context)
        } catch (e: Exception) {
            Log.e(TAG, "KeepAliveWorker failed to start background services", e)
        }
        return Result.success()
    }

    companion object {
        const val TAG = "KeepAliveWorker"
        const val WORK_NAME = "badgym_keepalive_periodic_watchdog"
    }
}
