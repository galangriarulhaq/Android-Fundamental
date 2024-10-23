package com.bangkit.eventdicodingapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class EventEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    var id: Int,

    @field:ColumnInfo(name = "mediaCover")
    var mediaCover: String,
    @field:ColumnInfo(name = "title")
    var title: String,
    @field:ColumnInfo(name = "owner")
    var owner: String,
    @field:ColumnInfo(name = "city")
    var city: String,
    @field:ColumnInfo(name = "category")
    var category: String,
    @field:ColumnInfo(name = "quota")
    var quota: Int,
    @field:ColumnInfo(name = "register")
    var register: Int,
    @field:ColumnInfo(name = "beginTime")
    var beginTime: String,
    @field:ColumnInfo(name = "event_status")
    var eventStatus: Boolean,
    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean,
)