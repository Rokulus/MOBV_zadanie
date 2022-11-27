package com.example.mobv_zadanie_procka.workers

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.mobv_zadanie_procka.R
import com.example.mobv_zadanie_procka.data.api.PubMessageRequest
import com.example.mobv_zadanie_procka.data.api.RestApi

class CheckoutWorker(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            1, createNotification()
        )
    }

    override suspend fun doWork(): Result {
        val response =
            RestApi.create(appContext).pubMessage(PubMessageRequest("", "", "", 0.0, 0.0))
        return if (response.isSuccessful) Result.success() else Result.failure()
    }

    private fun createNotification(): Notification {
        val builder =
            NotificationCompat.Builder(appContext, "mobv2022").apply {
                setContentTitle("MOBV 2022")
                setContentText("Exiting pub ...")
                setSmallIcon(R.drawable.ic_baseline_location_on_24)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        return builder.build()
    }


}