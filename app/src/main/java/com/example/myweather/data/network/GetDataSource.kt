package com.example.myweather.data.network

import com.example.myweather.data.model.ForecastData
import com.example.myweather.data.model.WeatherData
import org.json.JSONObject
import retrofit2.Response

interface GetDataSource {
    fun getWeatherInfo(
        jsonObject: JSONObject,
        onResponse: (Response<WeatherData>) -> Unit,
        onFailure: (Throwable) -> Unit
    )
    fun getForecastInfo(
        jsonObject: JSONObject,
        onResponse: (Response<ForecastData>) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}