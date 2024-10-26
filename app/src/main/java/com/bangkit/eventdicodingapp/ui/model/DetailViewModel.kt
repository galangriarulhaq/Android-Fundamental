package com.bangkit.eventdicodingapp.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.data.Result
import com.bangkit.eventdicodingapp.data.remote.response.EventDetailResponse
import com.bangkit.eventdicodingapp.util.EventWrapper

class DetailViewModel(private val eventRepository: EventRepository): ViewModel() {


    private val _eventDetail = MutableLiveData<EventDetailResponse>()
    val eventDetail: LiveData<EventDetailResponse> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<EventWrapper<String>>()
    val errorMessage: LiveData<EventWrapper<String>> = _errorMessage

    fun fetchDetailEvent(id: Int) {
        _isLoading.value = true

        eventRepository.fetchEventDetail(id).observeForever { result ->
            when (result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _eventDetail.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = EventWrapper("Error ${result.error}")
                }
            }
        }
    }
}