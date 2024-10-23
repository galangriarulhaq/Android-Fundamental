package com.bangkit.eventdicodingapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM events where event_status = 1 ORDER BY beginTime DESC")
    fun getEventUpcoming(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events where event_status = 0 ORDER BY beginTime DESC")
    fun getEventFinished(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events where favorite = 1 ORDER BY beginTime DESC")
    fun getEventsFavorite(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE favorite = 0 AND event_status = 1")
    suspend fun deleteUpcomingAll()

    @Query("DELETE FROM events WHERE favorite = 0 AND event_status = 0")
    suspend fun deleteFinishedAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :eventId AND favorite = 1)")
    suspend fun isEventFavorite(eventId: Int): Boolean

}