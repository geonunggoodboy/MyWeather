package com.example.myweather.domain.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.data.model.ForecastData
import com.example.myweather.data.model.WeatherData
import com.example.myweather.data.repository.WeatherRepository
import org.json.JSONObject

class WeatherViewModel(private val context: Context): ViewModel() {

    private val weatherRepository = WeatherRepository(context)

    val isSuccWeather = MutableLiveData<Boolean>()
    val isSuccForecast = MutableLiveData<Boolean>()
    val responseWeather = MutableLiveData<WeatherData>()
    val responseForecast = MutableLiveData<ForecastData>()


    fun getWeatherInfoView(jsonObject: JSONObject) {
        Log.d("ViewModel", "getWeatherInfoView() - jsonObject : $jsonObject")
        weatherRepository.getWeatherInfo(jsonObject = jsonObject,
            onResponse = {
                if (it.isSuccessful) {
                    Log.d("ViewModel", "getWeatherInfoView() - Succ : " + it.body())
                    isSuccWeather.value = true
                    responseWeather.value = it.body()
                } else {
                    Log.d("testValue", "getWeatherInfoView() - Not Success : " + it.code())
                }
            },
            onFailure = {
                it.printStackTrace()
                Log.d("testValue", "getWeatherInfoView() - Fail : " + it.message)
            }
        )
    }

    fun getForecastInfoView(jsonObject: JSONObject) {
        Log.d("ViewModel", "getForecastInfoView() - jsonObject : $jsonObject")
        weatherRepository.getForecastInfo(jsonObject = jsonObject,
            onResponse = {
                if (it.isSuccessful) {
                    Log.d("ViewModel", "getForecastInfoView() - Succ : " + it.body())
                    isSuccForecast.value = true
                    responseForecast.value = it.body()
                } else {
                    Log.d("testValue", "getForecastInfoView() - Not Success : " + it.code())
                }
            },
            onFailure = {
                it.printStackTrace()
                Log.d("testValue", "getForecastInfoView() - Fail : " + it.message)
            }
        )

    }

}