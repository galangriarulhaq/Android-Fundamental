package com.bangkit.eventdicodingapp.data.retrofit

import com.bangkit.eventdicodingapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("events?active=1")
    fun getEventUpcoming(): Call<EventResponse>

    @GET("events?active=0")
    fun getEventFinished(): Call<EventResponse>

}