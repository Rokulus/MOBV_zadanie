package com.example.mobv_zadanie_procka.helpers

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.mobv_zadanie_procka.data.DataRepository
import com.example.mobv_zadanie_procka.data.api.RestApi
import com.example.mobv_zadanie_procka.data.database.AppRoomDatabase
import com.example.mobv_zadanie_procka.data.database.LocalCache

object Injection {
    private fun provideCache(context: Context): LocalCache {
        val database = AppRoomDatabase.getInstance(context)
        return LocalCache(database.appDao())
    }

    fun provideDataRepository(context: Context): DataRepository {
        return DataRepository.getInstance(RestApi.create(context), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(
            provideDataRepository(
                context
            )
        )
    }
}