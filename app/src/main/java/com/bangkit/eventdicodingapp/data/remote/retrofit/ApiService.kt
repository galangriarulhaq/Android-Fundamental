package com.bangkit.eventdicodingapp.data.remote.retrofit

import com.bangkit.eventdicodingapp.data.remote.response.EventDetailResponse
import com.bangkit.eventdicodingapp.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    suspend fun getEventUpcoming(): EventResponse

    @GET("events?active=0")
    suspend fun getEventFinished(): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): EventDetailResponse

    @GET("events?active=-1")
    suspend fun getEventSearch(
        @Query("q") keyword: String
    ): EventResponse

    @GET("events")
    suspend fun getEventReminder(
        @Query("active") active: Int = -1,
        @Query("limit") limit: Int = 1
    ): EventResponse

}