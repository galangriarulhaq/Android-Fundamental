package com.bangkit.eventdicodingapp.di

import android.content.Context
import com.bangkit.eventdicodingapp.data.EventRepository
import com.bangkit.eventdicodingapp.data.local.room.EventDatabase
import com.bangkit.eventdicodingapp.data.remote.retrofit.ApiConfig
import com.bangkit.eventdicodingapp.util.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}