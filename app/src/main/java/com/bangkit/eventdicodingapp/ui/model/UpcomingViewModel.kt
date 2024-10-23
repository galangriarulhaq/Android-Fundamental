package com.bangkit.eventdicodingapp.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.eventdicodingapp.data.Result
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity
import com.bangkit.eventdicodingapp.util.EventWrapper


class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {

    companion object {
        private const val TAG = "UpcomingViewModel"
    }

    private val _listEvent = MutableLiveData<List<EventEntity>>()
    val listEvent: LiveData<List<EventEntity>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        fetchUpcomingEvent()
    }

    private fun fetchUpcomingEvent() {
        _isLoading.value = true

        eventRepository.fetchUpcomingEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listEvent.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }
    }

}