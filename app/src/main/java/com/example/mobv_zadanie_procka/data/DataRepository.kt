package com.example.mobv_zadanie_procka.data

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.mobv_zadanie_procka.data.api.*
import com.example.mobv_zadanie_procka.data.database.LocalCache
import com.example.mobv_zadanie_procka.data.database.model.Pub
import com.example.mobv_zadanie_procka.ui.viewmodels.data.MyLocation
import com.example.mobv_zadanie_procka.ui.viewmodels.data.NearbyPub
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DataRepository private constructor(
    private val service: RestApi,
    private val cache: LocalCache
){

    suspend fun apiUserCreate(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onStatus: (success: UserResponse?) -> Unit
    ) {
        try {
            val resp = service.userCreate(UserCreateRequest(name = name, password = password))
            if (resp.isSuccessful) {
                resp.body()?.let { user ->
                    if (user.uid == "-1"){
                        onStatus(null)
                        onError("Name already exists. Choose another.")
                    }else {
                        onStatus(user)
                    }
                }
            } else {
                onError("Failed to sign up, try again later.")
                onStatus(null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Sign up failed, check internet connection")
            onStatus(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Sign up failed, error.")
            onStatus(null)
        }
    }

    suspend fun apiUserLogin(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onStatus: (success: UserResponse?) -> Unit
    ) {
        try {
            val resp = service.userLogin(UserLoginRequest(name = name, password = password))
            if (resp.isSuccessful) {
                resp.body()?.let { user ->
                    if (user.uid == "-1"){
                        onStatus(null)
                        onError("Wrong name or password.")
                    }else {
                        onStatus(user)
                    }
                }
            } else {
                onError("Failed to login, try again later.")
                onStatus(null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Login failed, check internet connection")
            onStatus(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Login in failed, error.")
            onStatus(null)
        }
    }

    suspend fun apiPubCheckin(
        pub: NearbyPub,
        onError: (error: String) -> Unit,
        onSuccess: (success: Boolean) -> Unit
    ) {
        try {
            val resp = service.pubMessage(PubMessageRequest(pub.id.toString(),pub.name,pub.type,pub.lat,pub.lon))
            if (resp.isSuccessful) {
                resp.body()?.let { user ->
                    onSuccess(true)
                }
            } else {
                onError("Failed to login, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Login failed, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Login in failed, error.")
        }
    }

    suspend fun apiPubList(
        onError: (error: String) -> Unit
    ) {
        try {
            val resp = service.pubList()
            if (resp.isSuccessful) {
                resp.body()?.let { pubs ->

                    val b = pubs.map {
                        Pub(
                            it.pub_id,
                            it.pub_name,
                            it.pub_type,
                            it.lat,
                            it.lon,
                            it.users
                        )
                    }
                    cache.deletePubs()
                    cache.insertPubs(b)
                } ?: onError("Failed to load pubs")
            } else {
                onError("Failed to read pubs")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load pubs, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load pubs, error.")
        }
    }

    suspend fun apiNearbyPubs(
        lat: Double, lon: Double,
        onError: (error: String) -> Unit
    ) : List<NearbyPub> {
        var nearby = listOf<NearbyPub>()
        try {
            val q = "[out:json];node(around:250,$lat,$lon);(node(around:250)[\"amenity\"~\"^pub$|^pub$|^restaurant$|^cafe$|^fast_food$|^stripclub$|^nightclub$\"];);out body;>;out skel;"
            val resp = service.pubNearby(q)
            if (resp.isSuccessful) {
                resp.body()?.let { pubs ->
                    nearby = pubs.elements.map {
                        NearbyPub(it.id,it.tags.getOrDefault("name",""), it.tags.getOrDefault("amenity",""),it.lat,it.lon,it.tags).apply {
                            distance = distanceTo(MyLocation(lat,lon))
                        }
                    }
                    nearby = nearby.filter { it.name.isNotBlank() }.sortedBy { it.distance }
                } ?: onError("Failed to load pubs")
            } else {
                onError("Failed to read pubs")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load pubs, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load pubs, error.")
        }
        return nearby
    }

    suspend fun apiPubDetail(
        id: String,
        onError: (error: String) -> Unit
    ) : NearbyPub? {
        var nearby:NearbyPub? = null
        try {
            val q = "[out:json];node($id);out body;>;out skel;"
            val resp = service.pubDetail(q)
            if (resp.isSuccessful) {
                resp.body()?.let { pubs ->
                    if (pubs.elements.isNotEmpty()) {
                        val b = pubs.elements.get(0)
                        nearby = NearbyPub(
                            b.id,
                            b.tags.getOrDefault("name", ""),
                            b.tags.getOrDefault("amenity", ""),
                            b.lat,
                            b.lon,
                            b.tags
                        )
                    }
                } ?: onError("Failed to load pubs")
            } else {
                onError("Failed to read pubs")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load pubs, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load pubs, error.")
        }
        return nearby
    }

    fun dbPubs() : LiveData<List<Pub>?> {
        return cache.getPubs()
    }

    companion object{
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(service: RestApi, cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(service, cache).also { INSTANCE = it }
            }

        @SuppressLint("SimpleDateFormat")
        fun dateToTimeStamp(date: String): Long {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)?.time ?: 0L
        }

        @SuppressLint("SimpleDateFormat")
        fun timestampToDate(time: Long): String{
            val netDate = Date(time*1000)
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(netDate)
        }
    }
}