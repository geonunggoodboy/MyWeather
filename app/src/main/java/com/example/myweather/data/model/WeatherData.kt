package com.example.myweather.data.model

import com.google.gson.annotations.SerializedName

/* Full Model
   Example of information displayed
   city : seoul
   weather : <Clear, Clear sky, sun icon>
   main : <5, 22%>
   WindModel: 7.1m/s
   SysModel : Korea
   CloudModel: ~~
   */
data class WeatherData(
    @SerializedName("name")
    var name: String?, // City
    @SerializedName("weather")
    var weather: List<WeatherModel>,
    @SerializedName("main")
    var main: MainModel,
    @SerializedName("wind")
    var wind: WindModel,
    @SerializedName("sys")
    var sys: SysModel,
    @SerializedName("clouds")
    var cloud: CloudModel,
    @SerializedName("dt")
    var date: Double?,
    @SerializedName("timezone")
    var timezone: Double?
)

/* Weather Model */
data class WeatherModel(
    @SerializedName("main")
    var main: String?, // Weather
    @SerializedName("description")
    var description: String?, // Specific Weather
    @SerializedName("icon")
    var icon: String // Weather icon
)

/* Main Model */
data class MainModel(
    @SerializedName("temp")
    var temp: Double?, // Current Temperature
    @SerializedName("humidity")
    var humidity: Double?, // Current Humidity
    @SerializedName("temp_min")
    var temp_min: Double?,
    @SerializedName("temp_max")
    var temp_max: Double?
)

/* Wind Model */
data class WindModel(
    @SerializedName("speed")
    var speed: Double? // Wind
)

/* System Model */
data class SysModel(
    @SerializedName("country")
    var country: String?, // Country
    @SerializedName("sunrise")
    var sunrise: Double?,
    @SerializedName("sunset")
    var sunset: Double?
)

/* Clouds Model */
data class CloudModel(
    @SerializedName("all")
    var all: Double? // Clouds
)
