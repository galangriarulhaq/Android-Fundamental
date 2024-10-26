package com.bangkit.eventdicodingapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.eventdicodingapp.data.Result
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity
import com.bangkit.eventdicodingapp.util.EventWrapper


class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {


    private val _listEvent = MutableLiveData<List<EventEntity>>()
    val listEvent: LiveData<List<EventEntity>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>> = _errorMessage


    init {
        fetchUpcomingEvent()
    }

    suspend fun saveEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, true)
    }

    suspend fun deleteEvent(event: EventEntity) {
        eventRepository.setEventFavorite(event, false)
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
                    _errorMessage.value = EventWrapper("Error ${result.error}")
                }
            }
        }
    }

}