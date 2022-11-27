package com.example.mobv_zadanie_procka.ui.viewmodels

import androidx.lifecycle.*
import com.example.mobv_zadanie_procka.data.DataRepository
import com.example.mobv_zadanie_procka.helpers.Evento
import com.example.mobv_zadanie_procka.ui.viewmodels.data.NearbyPub
import com.example.mobv_zadanie_procka.ui.widget.detailList.PubDetail
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DataRepository) : ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    val loading = MutableLiveData(false)

    val pub = MutableLiveData<NearbyPub>(null)
    val type = pub.map { it?.tags?.getOrDefault("amenity", "") ?: "" }
    val details: LiveData<List<PubDetail>> = pub.switchMap {
        liveData {
            it?.let {
                emit(it.tags.map {
                    PubDetail(it.key, it.value)
                })
            } ?: emit(emptyList<PubDetail>())
        }
    }

    fun loadPub(id: String) {
        if (id.isBlank())
            return
        viewModelScope.launch {
            loading.postValue(true)
            pub.postValue(repository.apiPubDetail(id) { _message.postValue(Evento(it)) })
            loading.postValue(false)
        }
    }
}