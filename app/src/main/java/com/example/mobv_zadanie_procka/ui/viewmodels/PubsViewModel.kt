package com.example.mobv_zadanie_procka.ui.viewmodels

import androidx.lifecycle.*
import com.example.mobv_zadanie_procka.data.DataRepository
import com.example.mobv_zadanie_procka.data.database.model.Pub
import com.example.mobv_zadanie_procka.helpers.Evento
import kotlinx.coroutines.launch

class PubsViewModel(private val repository: DataRepository): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    val loading = MutableLiveData(false)

    val pubs: LiveData<List<Pub>?> =
        liveData {
            loading.postValue(true)
            repository.apiPubList { _message.postValue(Evento(it)) }
            loading.postValue(false)
            emitSource(repository.dbPubs())
        }

    fun refreshData(){
        viewModelScope.launch {
            loading.postValue(true)
            repository.apiPubList { _message.postValue(Evento(it)) }
            loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(Evento(msg))}
}