package com.example.myweather.data.repository

import android.content.Context
import com.example.myweather.data.model.ForecastData
import com.example.myweather.data.model.WeatherData
import com.example.myweather.data.network.GetDataSource
import com.example.myweather.data.network.GetDataSourceImpl
import org.json.JSONObject
import retrofit2.Response

class WeatherRepository(private val context: Context) {
    private val getDataSource: GetDataSource = GetDataSourceImpl(context)
    fun getWeatherInfo(
        jsonObject: JSONObject,
        onResponse: (Response<WeatherData>) -> Unit,
        onFailure: (Throwable) -> Unit
    ){
        getDataSource.getWeatherInfo(jsonObject, onResponse, onFailure)
    }
    fun getForecastInfo(
        jsonObject: JSONObject,
        onResponse: (Response<ForecastData>) -> Unit,
        onFailure: (Throwable) -> Unit
    ){
        getDataSource.getForecastInfo(jsonObject, onResponse, onFailure)
    }
}