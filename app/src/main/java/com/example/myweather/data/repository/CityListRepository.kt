package com.example.myweather.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myweather.data.room.CityList
import com.example.myweather.data.room.CityListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CityListRepository(private val cityListDao: CityListDao) {
    val cityList : LiveData<List<CityList>> = cityListDao.getAll()

    init {
        cityList.observeForever { Cities->
            Log.d("DataTest", "${Cities.size}")
        }
    }

    suspend fun insert(cityList: CityList){
        withContext(Dispatchers.IO){
            cityListDao.insert(cityList)
        }
    }

    fun delete(cityList: CityList){
        cityListDao.delete(cityList)
    }
}