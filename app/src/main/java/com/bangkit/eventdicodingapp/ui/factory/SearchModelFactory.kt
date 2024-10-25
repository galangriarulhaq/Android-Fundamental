package com.bangkit.eventdicodingapp.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.di.Injection
import com.bangkit.eventdicodingapp.ui.model.SearchViewModel

class SearchModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SearchModelFactory? = null
        fun getInstance(context: Context): SearchModelFactory =
            instance ?: synchronized(this) {
                instance ?: SearchModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}