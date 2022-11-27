package com.example.mobv_zadanie_procka.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobv_zadanie_procka.data.DataRepository
import com.example.mobv_zadanie_procka.ui.viewmodels.AuthViewModel
import com.example.mobv_zadanie_procka.ui.viewmodels.DetailViewModel
import com.example.mobv_zadanie_procka.ui.viewmodels.LocateViewModel
import com.example.mobv_zadanie_procka.ui.viewmodels.PubsViewModel

class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(PubsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PubsViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(LocateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocateViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}