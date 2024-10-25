package com.bangkit.eventdicodingapp.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.di.Injection
import com.bangkit.eventdicodingapp.ui.model.FavoriteViewModel

class FavoriteModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoriteModelFactory? = null
        fun getInstance(context: Context): FavoriteModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavoriteModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}