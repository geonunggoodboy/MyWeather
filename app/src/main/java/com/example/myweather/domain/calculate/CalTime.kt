package com.example.myweather.domain.calculate

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class CalTime {
    fun convertUnixTime(dt: Double?, timeZone: Double?): String{
        requireNotNull(dt) {"dt is null"}
        requireNotNull(timeZone) {"timezone is null"}
        val dataFormat = SimpleDateFormat("MMM d, h:mma", Locale.getDefault())
        dataFormat.timeZone = SimpleTimeZone((timeZone * 1000L).toInt(), "")
        val date = Date(dt.toLong() * 1000L)
        Log.d("timeUnix", "${dt.toLong()}")
        Log.d("timeUnix", "$date")
        return dataFormat.format(date)
    }

    fun isDayTime(dt: Double?,sunRiseTime: Double?, sunSetTime: Double?): Boolean{
        requireNotNull(dt) {"dt is null"}
        requireNotNull(sunRiseTime) {"sunRiseTime is null"}
        requireNotNull(sunSetTime) {"sunSetTime is null"}
        return dt in sunRiseTime..sunSetTime
    }
}