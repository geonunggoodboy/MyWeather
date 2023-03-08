package com.example.myweather.data.network

import com.example.myweather.data.model.ForecastData
import com.example.myweather.data.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("data/2.5/{path}")
    fun getWeatherData(
        @Path("path") path: String,
        @Query("q") q: String,
        @Query("appid") appid: String
    ): Call<WeatherData>
    @GET("data/2.5/{path}")
    fun getForecastData(
        @Path("path") path: String,
        @Query("q") q: String,
        @Query("appid") appid: String
    ): Call<ForecastData>
}