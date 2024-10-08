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

class HomeViewModel : ViewModel() {

    companion object {
        private const val TAG = "EventModel"
    }

    private val _listEventUpcoming = MutableLiveData<List<ListEventsItem>>()
    val listEventUpcoming: LiveData<List<ListEventsItem>> = _listEventUpcoming

    private val _listEventFinished = MutableLiveData<List<ListEventsItem>>()
    val listEventFinished: LiveData<List<ListEventsItem>> = _listEventFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchEventUpcoming()
        fetchEventFinished()
    }

    private fun fetchEventUpcoming() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventUpcoming()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEventUpcoming.value = responseBody?.listEvents
                } else {
                    Log.e(HomeViewModel.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(HomeViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }



    private fun fetchEventFinished() {
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
                    _listEventFinished.value = responseBody?.listEvents
                } else {
                    Log.e(HomeViewModel.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(HomeViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}