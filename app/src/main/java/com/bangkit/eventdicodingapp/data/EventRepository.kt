package com.bangkit.eventdicodingapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity
import com.bangkit.eventdicodingapp.data.local.room.EventDao
import com.bangkit.eventdicodingapp.data.remote.response.EventDetailResponse
import com.bangkit.eventdicodingapp.data.remote.response.ListEventsItem
import com.bangkit.eventdicodingapp.data.remote.retrofit.ApiService
import com.bangkit.eventdicodingapp.util.AppExecutors
import retrofit2.HttpException

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun fetchActiveEvents(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventUpcoming()
            val events = response.listEvents
            val eventList = events.map { event ->
                val isFavorite = eventDao.isEventFavorite(event.id)
                EventEntity(
                    event.id,
                    event.mediaCover,
                    event.name,
                    event.ownerName,
                    event.cityName,
                    event.category,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    true,
                    isFavorite
                )
            }

            // Update local database
            eventDao.deleteUpcomingAll()
            eventDao.insertEvent(eventList)

            // Emit local data after saving it
            val localData: LiveData<Result<List<EventEntity>>> =
                eventDao.getEventUpcoming().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            Log.e("EventRepository", "fetchActiveEvents: ${e.message.toString()} ", e)
            emit(Result.Error(e.message.toString()))
        }
    }

    fun fetchFinishedEvents(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            // Fetch data from remote
            val response = apiService.getEventFinished()
            val events = response.listEvents
            val eventList = events.map { event ->
                val isFavorite = eventDao.isEventFavorite(event.id)
                EventEntity(
                    event.id,
                    event.mediaCover,
                    event.name,
                    event.ownerName,
                    event.cityName,
                    event.category,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    false,
                    isFavorite
                )
            }

            // Update local database
            eventDao.deleteFinishedAll()
            eventDao.insertEvent(eventList)

            // Emit local data after saving it
            val localData: LiveData<Result<List<EventEntity>>> =
                eventDao.getEventFinished().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            Log.e("EventRepository", "fetchNonActiveEvents: ${e.message.toString()} ", e)
            emit(Result.Error(e.message.toString()))
        }
    }

    fun fetchEventDetail(id: Int): LiveData<Result<EventDetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventDetail(id)
            if (!response.error) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error("Failed to load event details: ${response.message}"))
            }
        } catch (e: HttpException) {
            Log.e("EventRepository", "fetchEventDetail: ${e.message()}", e)
            emit(Result.Error("Error: ${e.message()}"))
        } catch (e: Exception) {
            Log.e("EventRepository", "fetchEventDetail: ${e.message.toString()}", e)
            emit(Result.Error(e.message.toString()))
        }
    }

    fun fetchSearchEvent(query: String): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventSearch(query)
            if (response.listEvents.isNotEmpty()) {
                val events = response.listEvents
                emit(Result.Success(events))
            } else {
                emit(Result.Error("Error: empty"))
            }
        } catch (e: Exception) {
            Log.e("EventRepository", "fetchSearchEvent: ${e.message.toString()} ", e)
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getEventFavorite(): LiveData<List<EventEntity>> {
        return eventDao.getEventsFavorite()
    }

    suspend fun setEventFavorite(events: EventEntity, eventState: Boolean) {
        events.isFavorite = eventState
        eventDao.updateEvent(events)
    }


    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }


}