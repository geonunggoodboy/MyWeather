package com.example.myweather.data.network

import android.content.Context
import android.util.Log
import com.example.myweather.data.model.ForecastData
import com.example.myweather.data.model.WeatherData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetDataSourceImpl(private val context: Context): GetDataSource {
    override fun getWeatherInfo(
        jsonObject: JSONObject,
        onResponse: (Response<WeatherData>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val weatherAPIService = WeatherAPIClient.getClient(context, jsonObject.getString("url"))
        weatherAPIService.getWeatherData(jsonObject.getString("path"), jsonObject.getString("q"), jsonObject.getString("appid"))
            .enqueue(object : Callback<WeatherData>{
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    onResponse(response)
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    override fun getForecastInfo(
        jsonObject: JSONObject,
        onResponse: (Response<ForecastData>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val weatherAPIService = WeatherAPIClient.getClient(context, jsonObject.getString("url"))
        weatherAPIService.getForecastData(jsonObject.getString("path"), jsonObject.getString("q"), jsonObject.getString("appid"))
            .enqueue(object : Callback<ForecastData>{
                override fun onResponse(
                    call: Call<ForecastData>,
                    response: Response<ForecastData>
                ) {
                    onResponse(response)
                }

                override fun onFailure(call: Call<ForecastData>, t: Throwable) {
                    onFailure(t)
                }
            })
    }
}