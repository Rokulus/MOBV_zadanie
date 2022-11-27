package com.example.mobv_zadanie_procka.data.api.apiHelper

import android.content.Context
import com.example.mobv_zadanie_procka.helpers.Config
import com.example.mobv_zadanie_procka.helpers.PreferenceData
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val request = chain.request()
                .newBuilder()
                .addHeader("User-Agent", "Mobv-Android/1.0.0")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            if (chain.request().header("mobv-auth")?.compareTo("accept") == 0) {
                request.addHeader(
                    "Authorization",
                    "Bearer ${PreferenceData.getInstance().getUserItem(context)?.access}"
                )

            }
            PreferenceData.getInstance().getUserItem(context)?.uid?.let {
                request.addHeader(
                    "x-user",
                    it
                )
            }
            request.addHeader("x-apikey", Config.API_KEY)

            val response = chain.proceed(request.build())
            return response
        }
    }

}