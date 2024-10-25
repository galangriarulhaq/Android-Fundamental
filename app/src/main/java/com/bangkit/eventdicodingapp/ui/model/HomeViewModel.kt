package com.bangkit.eventdicodingapp.ui.model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.data.Result
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity
import com.bangkit.eventdicodingapp.util.EventWrapper

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }


    private val _listEventUpcoming = MutableLiveData<List<EventEntity>>()
    val listEventUpcoming: LiveData<List<EventEntity>> = _listEventUpcoming

    private val _listEventFinished = MutableLiveData<List<EventEntity>>()
    val listEventFinished: LiveData<List<EventEntity>> = _listEventFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
//    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
//    val errorMessage: LiveData<EventWrapper<String>> = _errorMessage

    init {
        fetchEventUpcoming()
        fetchEventFinished()
    }

    private fun fetchEventUpcoming() {
        _isLoading.value = true

        eventRepository.fetchUpcomingEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listEventUpcoming.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }
    }



    private fun fetchEventFinished() {
        _isLoading.value = true

        eventRepository.fetchFinishedEvents().observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listEventFinished.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }
    }
}