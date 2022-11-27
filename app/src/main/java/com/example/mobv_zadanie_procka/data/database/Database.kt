package com.example.mobv_zadanie_procka.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobv_zadanie_procka.data.database.model.Pub

@Database(entities = [Pub::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase: RoomDatabase() {

    abstract fun appDao(): PubDao

    companion object{
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {  INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java, "pubsDatabase"
            ).fallbackToDestructiveMigration()
                .build()
    }

}