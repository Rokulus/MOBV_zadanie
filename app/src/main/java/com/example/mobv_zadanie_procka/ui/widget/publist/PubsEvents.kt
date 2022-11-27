package com.example.mobv_zadanie_procka.ui.widget.publist

import com.example.mobv_zadanie_procka.data.database.model.Pub

interface PubsEvents {
    fun onPubClick(pub: Pub)
}