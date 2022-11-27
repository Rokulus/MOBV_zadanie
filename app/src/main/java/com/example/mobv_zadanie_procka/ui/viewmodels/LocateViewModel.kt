package com.example.mobv_zadanie_procka.ui.viewmodels

import androidx.lifecycle.*
import com.example.mobv_zadanie_procka.data.DataRepository
import com.example.mobv_zadanie_procka.helpers.Evento
import com.example.mobv_zadanie_procka.ui.viewmodels.data.MyLocation
import com.example.mobv_zadanie_procka.ui.viewmodels.data.NearbyPub
import kotlinx.coroutines.launch

class LocateViewModel(private val repository: DataRepository): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    val loading = MutableLiveData(false)

    val myLocation = MutableLiveData<MyLocation>(null)
    val myPub= MutableLiveData<NearbyPub>(null)

    private val _checkedIn = MutableLiveData<Evento<Boolean>>()
    val checkedIn: LiveData<Evento<Boolean>>
        get() = _checkedIn


    val pubs : LiveData<List<NearbyPub>> = myLocation.switchMap {
        liveData {
            loading.postValue(true)
            it?.let {
                val b = repository.apiNearbyPubs(it.lat,it.lon,{_message.postValue(Evento(it))})
                emit(b)
                if (myPub.value==null){
                    myPub.postValue(b.firstOrNull())
                }
            } ?: emit(listOf())
            loading.postValue(false)
        }
    }

    fun checkMe(){
        viewModelScope.launch {
            loading.postValue(true)
            myPub.value?.let {
                repository.apiPubCheckin(
                    it,
                    {_message.postValue(Evento(it))},
                    {_checkedIn.postValue(Evento(it))})
            }
            loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(Evento(msg))}
}