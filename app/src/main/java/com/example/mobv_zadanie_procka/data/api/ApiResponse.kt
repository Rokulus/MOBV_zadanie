package com.example.mobv_zadanie_procka.data.api

data class UserResponse(
    val uid: String,
    val access: String,
    val refresh: String
)

data class PubListResponse(
    val pub_id: String,
    val pub_name: String,
    val pub_type: String,
    val lat: Double,
    var lon: Double,
    var users: Int
)

data class PubDetailItemResponse(
    val type: String,
    val id: String,
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>
)

data class PubDetailResponse(
    val elements: List<PubDetailItemResponse>
)