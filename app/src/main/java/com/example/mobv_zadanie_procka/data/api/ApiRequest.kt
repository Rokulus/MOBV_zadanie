package com.example.mobv_zadanie_procka.data.api

data class UserCreateRequest(
    val name: String,
    val password: String
)

data class UserLoginRequest(
    val name: String,
    val password: String
)

data class UserRefreshRequest(
    val refresh: String
)

data class PubMessageRequest(
    val id: String,
    val name: String,
    val type: String,
    val lat: Double,
    val lon: Double
)