package com.example.myweather.data.model

import com.google.gson.annotations.SerializedName

data class ForecastData(
    @SerializedName("cod")
    var cod: String?,
    @SerializedName("cnt")
    var cnt: Int?,
    @SerializedName("list")
    var list: List<ForecastListModel>,
    @SerializedName("city")
    val city: CityModel
)

data class ForecastListModel(
    @SerializedName("dt")
    var dt: Double?, // Unix Time
    @SerializedName("main")
    var main: MainModel?,
    @SerializedName("weather")
    var weather: List<WeatherModel>
)

data class CityModel(
    @SerializedName("name")
    var name: String?,
    @SerializedName("timezone")
    var timeZone: Double?
)
