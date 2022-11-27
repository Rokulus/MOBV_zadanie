package com.example.mobv_zadanie_procka.data.api

import android.content.Context
import com.example.mobv_zadanie_procka.data.api.apiHelper.ApiAuthenticator
import com.example.mobv_zadanie_procka.data.api.apiHelper.ApiInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RestApi {

    @GET("https://overpass-api.de/api/interpreter?")
    suspend fun pubDetail(@Query("data") data: String): Response<BarDetailResponse>

    @GET("https://overpass-api.de/api/interpreter?")
    suspend fun pubNearby(@Query("data") data: String): Response<BarDetailResponse>

    @POST("user/create.php")
    suspend fun userCreate(@Body user: UserCreateRequest): Response<UserResponse>

    @POST("user/login.php")
    suspend fun userLogin(@Body user: UserLoginRequest): Response<UserResponse>

//    @POST("user/refresh.php")
//    suspend fun userRefresh(@Body user: UserRefreshRequest) : Response<UserResponse>

    @POST("user/refresh.php")
    fun userRefresh(@Body user: UserRefreshRequest) : Call<UserResponse>

    @GET("bar/list.php")
    @Headers("mobv-auth: accept")
    suspend fun pubList() : Response<List<BarListResponse>>

    @POST("bar/message.php")
    @Headers("mobv-auth: accept")
    suspend fun pubMessage(@Body pub: BarMessageRequest) : Response<Any>

    companion object{
        const val BASE_URL = "https://zadanie.mpage.sk/"

        fun create(context: Context): RestApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(ApiInterceptor(context))
                .authenticator(ApiAuthenticator(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RestApi::class.java)
        }
    }

}