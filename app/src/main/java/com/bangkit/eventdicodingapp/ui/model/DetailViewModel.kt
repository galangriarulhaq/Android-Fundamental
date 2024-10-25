package com.bangkit.eventdicodingapp.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.data.remote.response.Event
import com.bangkit.eventdicodingapp.data.Result
import com.bangkit.eventdicodingapp.data.remote.response.EventDetailResponse
import com.bangkit.eventdicodingapp.data.remote.retrofit.ApiConfig
import com.bangkit.eventdicodingapp.util.EventWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val eventRepository: EventRepository): ViewModel() {

    companion object {
        private const val TAG = "DetailViewModel"
    }

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
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }
    }
}