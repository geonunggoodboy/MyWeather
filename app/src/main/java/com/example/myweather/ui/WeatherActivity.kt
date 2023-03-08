package com.example.myweather.ui

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myweather.R
import com.example.myweather.data.model.ForecastData
import com.example.myweather.data.model.WeatherData
import com.example.myweather.databinding.ActivityWeatherBinding
import com.example.myweather.databinding.SearchDialogBinding
import com.example.myweather.domain.adapter.ListAdapter
import com.example.myweather.domain.calculate.CalLocation
import com.example.myweather.domain.calculate.CalTime
import com.example.myweather.domain.viewModel.WeatherViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*
import kotlin.math.abs

class WeatherActivity : AppCompatActivity() {

    /* Global Variables */
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var searchDialog: Dialog
    private lateinit var searchBinding: SearchDialogBinding
    private lateinit var calLocation: CalLocation

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var calTime: CalTime
    private lateinit var listAdapter: ListAdapter

    private var weatherData: WeatherData? = null
    private var forecastData: ForecastData? = null

    private var jsonObjectW = JSONObject()
    private var jsonObjectF = JSONObject()
    private var isActive = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calLocation = CalLocation(this)
        calLocation.checkPermission()
        calTime = CalTime()

        searchBinding = SearchDialogBinding.inflate(layoutInflater)
        searchDialog = Dialog(this)

        calLocation.getCurrentCityName { cityName ->
            initJsonW(cityName)
            initJsonF(cityName)
            checkCollapsedAppbar()
            initViewModel()
            initAdapter()
            initSearchDialog()
            updateLocation()
        }

        binding.fabSearch.setOnClickListener{
            showSearchDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        isActive = true
    }

    override fun onPause() {
        super.onPause()
        isActive = false
    }


    private fun initJsonW(currCityName: String){
        jsonObjectW.put("q", currCityName)
        jsonObjectW.put("url", getString(R.string.weather_url))
        jsonObjectW.put("path", "weather")
        jsonObjectW.put("appid", getString(R.string.weather_app_id))
    }

    private fun initJsonF(currCityName: String){
        jsonObjectF.put("q", currCityName)
        jsonObjectF.put("url", getString(R.string.weather_url))
        jsonObjectF.put("path", "forecast")
        jsonObjectF.put("appid", getString(R.string.weather_app_id))
    }

    private fun initViewModel(){
        weatherViewModel = WeatherViewModel(this)
        weatherViewModel.getWeatherInfoView(jsonObjectW)
        weatherViewModel.getForecastInfoView(jsonObjectF)
    }

    private fun initAdapter(){
        listAdapter = ListAdapter(this)
        binding.rvForecast.apply {
            layoutManager = GridLayoutManager(this@WeatherActivity, 3)
            adapter = listAdapter
        }
    }

    private fun initSearchDialog() {
        searchBinding = SearchDialogBinding.inflate(layoutInflater)
        searchDialog = Dialog(this)
        searchDialog.setContentView(searchBinding.root)
        searchDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        searchBinding.btnSearch.setOnClickListener {
            val city = searchBinding.etLocation.text.toString()
            if (city.isNotBlank()) {
                jsonObjectW.put("q", city)
                jsonObjectF.put("q", city)
                weatherViewModel.getWeatherInfoView(jsonObjectW)
                weatherViewModel.getForecastInfoView(jsonObjectF)
            }
            hideKeyboard()
            searchDialog.hide()
        }
        searchBinding.etLocation.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val city = searchBinding.etLocation.text.toString()
                if (city.isNotBlank()) {
                    jsonObjectW.put("q", city)
                    jsonObjectF.put("q", city)
                    weatherViewModel.getWeatherInfoView(jsonObjectW)
                    weatherViewModel.getForecastInfoView(jsonObjectF)
                }
                hideKeyboard()
                searchDialog.hide()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateLocation(){
        GlobalScope.launch(Dispatchers.IO) {
            while(isActive){
                weatherViewModel.getWeatherInfoView(jsonObjectW)
                weatherViewModel.getForecastInfoView(jsonObjectF)
                withContext(Dispatchers.Main){
                    observeData()
                }
                delay(300000)
            }
        }
    }

    private fun showSearchDialog() {
        searchBinding.etLocation.setText("")
        searchDialog.show()
    }

    private fun observeData() {
        weatherViewModel.isSuccWeather.observe(this, Observer { it ->
            if (it) {
                weatherViewModel.responseWeather.observe(this, Observer { weather ->
                    weatherData = weather
                    setWeatherData(weatherData)
                })
            } else {
                // Do nothing
            }
        })

        weatherViewModel.isSuccForecast.observe(this, Observer { it ->
            Log.d("forecast", "isSuccForecast")
            if (it) {
                weatherViewModel.responseForecast.observe(this, Observer { forecast ->
                    Log.d("forecast", "responseForecast")
                    listAdapter.clearForecastData()
                    forecastData = forecast
                    listAdapter.setForecastData(forecastData)
                })
            } else {
                // Do nothing
            }
        })
    }

    private fun setInitialBackgroundColor(weatherData: WeatherData?) {
        val isDayTime = calTime.isDayTime(weatherData?.date, weatherData?.sys?.sunrise, weatherData?.sys?.sunset)
        val backgroundColor = if (isDayTime) {
            getColor(R.color.blue_500)
        } else {
            getColor(R.color.black)
        }
        binding.CLWeather.setBackgroundColor(backgroundColor)
    }

    private fun setWeatherData(model: WeatherData?){
        Log.d("setWeather", "실행")
        requireNotNull(model)
        binding.apply {
            tvCity.text = model.name
            tvCityIconCollapsed.text = model.name
            tvCountry.text = model.sys.country
            tvCountryIconCollapsed.text = model.sys.country
            tvData.text = model.weather[0].main
            tvWeatherDescription.text = model.weather[0].description
            tvTemperature.text = String.format("%.2f °C", model.main.temp?.minus(273.15))
            tvTemperatureCollapsed.text = String.format("%.2f °C", model.main.temp?.minus(273.15))
            tvHumidity.text = String.format("%.2f %%", model.main.humidity)
            tvCloud.text = String.format("%.2f %%", model.cloud.all)
            tvWindSpeed.text = String.format("%.2f m/s", model.wind.speed)
            tvDate.text = calTime.convertUnixTime(model.date, model.timezone)
            tvDateCollapsed.text = calTime.convertUnixTime(model.date, model.timezone)
            tvTempMax.text = String.format("%.2f °C", model.main.temp_max?.minus(273.15))
            tvTempMin.text = String.format("%.2f °C", model.main.temp_min?.minus(273.15))
            tvTempMaxCollapsed.text = String.format("%.2f °C", model.main.temp_max?.minus(273.15))
            tvTempMinCollapsed.text = String.format("%.2f °C", model.main.temp_min?.minus(273.15))
        }
        setInitialBackgroundColor(model)
        val weatherIcon = model.weather[0].icon
        val iconResource = when (weatherIcon) {
            "01d" -> R.drawable.icon_sun
            "01n" -> R.drawable.icon_moon
            "02d" -> R.drawable.icon_sun_with_cloud
            "02n" -> R.drawable.icon_moon_with_cloud
            "03d" -> R.drawable.icon_clouds
            "03n" -> R.drawable.icon_clouds
            "04d" -> R.drawable.icon_clouds
            "04n" -> R.drawable.icon_clouds
            "09d" -> R.drawable.icon_rain
            "09n" -> R.drawable.icon_rain
            "10d" -> R.drawable.icon_rain
            "10n" -> R.drawable.icon_rain
            "11d" -> R.drawable.icon_thunderstorm
            "11n" -> R.drawable.icon_thunderstorm
            "13d" -> R.drawable.icon_snow
            "13n" -> R.drawable.icon_snow
            "50d" -> R.drawable.icon_fog
            "50n" -> R.drawable.icon_fog
            else -> R.drawable.icon_sun
        }
        binding.ivWeatherIcon.setImageResource(iconResource)
        binding.ivWeatherIconCollapsed.setImageResource(iconResource)
    }

    private fun hideKeyboard(){
        searchBinding.etLocation.text = null
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkCollapsedAppbar() {
        // set bal
        val rlCollapsedView = binding.RLCollapsedView
        val llCollapsedView = binding.LLCollapsedView

        // control rlView
        val rlCollapseAnimator = ObjectAnimator.ofFloat(rlCollapsedView, "translationY", -rlCollapsedView.height.toFloat()).setDuration(300)
        val rlAlphaAnimator = ObjectAnimator.ofFloat(rlCollapsedView, "alpha", 0f).setDuration(300)
        val rlExpandAnimator = ObjectAnimator.ofFloat(rlCollapsedView, "translationY", 0f).setDuration(300)
        val rlAlphaAnimator2 = ObjectAnimator.ofFloat(rlCollapsedView, "alpha", 1f).setDuration(300)

        // set llView
        val llCollapseAnimator = ObjectAnimator.ofFloat(llCollapsedView, "translationY", -rlCollapsedView.height.toFloat()).setDuration(300)
        val llAlphaAnimator = ObjectAnimator.ofFloat(llCollapsedView, "alpha", 1f).setDuration(300)
        val llExpandAnimator = ObjectAnimator.ofFloat(llCollapsedView, "translationY", 1f).setDuration(300)
        val llAlphaAnimator2 = ObjectAnimator.ofFloat(llCollapsedView, "alpha", 0f).setDuration(300)

        // set Animation
        val set = AnimatorSet()
        set.playTogether(rlCollapseAnimator, rlAlphaAnimator)
        set.playTogether(rlExpandAnimator, rlAlphaAnimator2)
        set.playTogether(llCollapseAnimator, llAlphaAnimator)
        set.playTogether(llExpandAnimator, llAlphaAnimator2)

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
            set.currentPlayTime = (percentage * 300).toLong()
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            101 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "앱 실행을 위한 권한이 취소 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}