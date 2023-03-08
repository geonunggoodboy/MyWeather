package com.example.myweather.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cities")
data class CityList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "city") var name: String?,
    var url: UrlList,
    var path: PathList,
    var state: StateList
)

data class UrlList(
    var weatherUrl: String?,
    var forecastUrl: String?
)

data class PathList(
    var weatherPath: String?,
    var forecastPath: String?
)

data class StateList(
    var currTemp: Double?,
    var icon: String?
)