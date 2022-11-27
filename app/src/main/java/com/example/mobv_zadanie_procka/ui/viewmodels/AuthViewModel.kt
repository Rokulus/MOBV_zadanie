package com.example.mobv_zadanie_procka.ui.viewmodels

import androidx.lifecycle.*
import com.example.mobv_zadanie_procka.data.DataRepository
import com.example.mobv_zadanie_procka.data.api.UserResponse
import com.example.mobv_zadanie_procka.helpers.Evento
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: DataRepository): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()
    val message: LiveData<Evento<String>>
        get() = _message

    val user= MutableLiveData<UserResponse>(null)

    val loading = MutableLiveData(false)

    fun login(name: String, password: String){
        viewModelScope.launch {
            loading.postValue(true)
            repository.apiUserLogin(
                name,password,
                { _message.postValue(Evento(it)) },
                { user.postValue(it) }
            )
            loading.postValue(false)
        }
    }

    fun signup(name: String, password: String){
        viewModelScope.launch {
            loading.postValue(true)
            repository.apiUserCreate(
                name,password,
                { _message.postValue(Evento(it)) },
                { user.postValue(it) }
            )
            loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(Evento(msg))}
}