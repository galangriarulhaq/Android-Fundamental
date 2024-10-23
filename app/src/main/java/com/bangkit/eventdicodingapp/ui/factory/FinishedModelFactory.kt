package com.bangkit.eventdicodingapp.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.di.Injection
import com.bangkit.eventdicodingapp.ui.model.FinishedViewModel
import com.bangkit.eventdicodingapp.ui.model.UpcomingViewModel

class FinishedModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishedViewModel::class.java)) {
            return FinishedViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FinishedModelFactory? = null
        fun getInstance(context: Context): FinishedModelFactory =
            instance ?: synchronized(this) {
                instance ?: FinishedModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}