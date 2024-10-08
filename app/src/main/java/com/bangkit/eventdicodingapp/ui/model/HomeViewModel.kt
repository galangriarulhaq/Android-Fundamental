package com.bangkit.eventdicodingapp.ui.model


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


    private val _listEventUpcoming = MutableLiveData<List<ListEventsItem>>()
    val listEventUpcoming: LiveData<List<ListEventsItem>> = _listEventUpcoming

    private val _listEventFinished = MutableLiveData<List<ListEventsItem>>()
    val listEventFinished: LiveData<List<ListEventsItem>> = _listEventFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = _snackbarText

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
                    _snackbarText.value = response.body()?.message
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = "Network Error : ${t.message.toString()}"
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
                    _snackbarText.value = response.body()?.message
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = "Network Error : ${t.message.toString()}"
            }
        })
    }
}