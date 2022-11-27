package com.example.mobv_zadanie_procka.data.database

import androidx.lifecycle.LiveData
import com.example.mobv_zadanie_procka.data.database.model.Pub

class LocalCache(private val dao: PubDao) {
    suspend fun insertPubs(pubs: List<Pub>){
        dao.insertPubs(pubs)
    }

    suspend fun deletePubs(){ dao.deletePubs() }

    fun getPubs(): LiveData<List<Pub>?> = dao.getPubs()
}