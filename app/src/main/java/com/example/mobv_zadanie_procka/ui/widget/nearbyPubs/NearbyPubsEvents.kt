package com.example.mobv_zadanie_procka.ui.widget.nearbyPubs

import com.example.mobv_zadanie_procka.ui.viewmodels.data.NearbyPub

interface NearbyPubsEvents {
    fun onPubClick(nearbyPub: NearbyPub)
}