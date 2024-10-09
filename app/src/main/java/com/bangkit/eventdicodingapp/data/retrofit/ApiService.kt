package com.bangkit.eventdicodingapp.data.retrofit

import com.bangkit.eventdicodingapp.data.response.EventDetailResponse
import com.bangkit.eventdicodingapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    fun getEventUpcoming(): Call<EventResponse>

    @GET("events?active=0")
    fun getEventFinished(): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<EventDetailResponse>

    @GET("events")
    suspend fun getEventSearch(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): EventResponse

}