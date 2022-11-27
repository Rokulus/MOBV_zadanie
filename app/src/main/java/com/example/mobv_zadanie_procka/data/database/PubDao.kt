package com.example.mobv_zadanie_procka.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobv_zadanie_procka.data.database.model.Pub

@Dao
interface PubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPubs(bars: List<Pub>)

    @Query("DELETE FROM pubs")
    suspend fun deletePubs()

    @Query("SELECT * FROM pubs order by users DESC, name ASC")
    fun getPubs(): LiveData<List<Pub>?>

}