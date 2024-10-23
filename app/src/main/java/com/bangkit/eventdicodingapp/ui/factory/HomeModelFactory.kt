package com.bangkit.eventdicodingapp.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.di.Injection
import com.bangkit.eventdicodingapp.ui.model.FinishedViewModel
import com.bangkit.eventdicodingapp.ui.model.HomeViewModel

class HomeModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeModelFactory? = null
        fun getInstance(context: Context): HomeModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}