package com.bangkit.eventdicodingapp.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.eventdicodingapp.data.response.EventResponse
import com.bangkit.eventdicodingapp.data.response.ListEventsItem
import com.bangkit.eventdicodingapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    companion object {
        private const val TAG = "EventModel"
    }
    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchEvent()
    }

    private fun fetchEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventFinished()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvent.value = responseBody?.listEvents
                } else {
                    Log.e(FinishedViewModel.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(FinishedViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}