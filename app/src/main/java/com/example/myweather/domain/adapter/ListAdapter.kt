package com.example.myweather.domain.adapter

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.data.model.CityModel
import com.example.myweather.data.model.ForecastData
import com.example.myweather.data.model.ForecastListModel
import com.example.myweather.databinding.ItemListBinding
import com.example.myweather.domain.calculate.CalTime

class ListAdapter(private val context: Context) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var forecastData: ForecastData = ForecastData("", 0, emptyList(), CityModel(null, null))
    private lateinit var calTime: CalTime

    inner class ListViewHolder(private val listBinding: ItemListBinding) : RecyclerView.ViewHolder(listBinding.root) {
        fun bind(forecast: ForecastListModel, timeZone: Double) {
            calTime = CalTime()
            listBinding.apply {
                tvDetailDate.text = calTime.convertUnixTime(forecast.dt, timeZone)
                tvDetailMain.text = forecast.weather[0].main
                tvDetailTemp.text = forecast.main?.let { String.format("%.2f °C", it.temp?.minus(273.15)) }
            }
            val weatherIcon = forecast.weather[0].icon
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
            listBinding.ivDetailImg.setImageResource(iconResource)
        }
    }

    fun setForecastData(forecastData: ForecastData?) {
        requireNotNull(forecastData)
        this.forecastData = forecastData
        notifyDataSetChanged()
    }

    fun clearForecastData() {
        forecastData = ForecastData("", 0, emptyList(), CityModel("", null))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return forecastData.list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val forecast = forecastData.list[position]
        holder.bind(forecast, forecastData.city.timeZone!!)
    }
}
