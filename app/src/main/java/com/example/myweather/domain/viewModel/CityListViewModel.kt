package com.example.myweather.domain.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.repository.CityListRepository
import com.example.myweather.data.room.CityDatabase
import com.example.myweather.data.room.CityList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityListViewModel(application: Application): AndroidViewModel(application) {
    private val cityListRepository: CityListRepository
    private val _cityList: LiveData<List<CityList>>

    init {
        val cityListDao = checkNotNull(CityDatabase.getDatabase(application)){"Database is not initialized"}.cityListDao()
        cityListRepository = CityListRepository(cityListDao)
        _cityList = cityListRepository.cityList
    }

    fun getAll(): LiveData<List<CityList>>{
        return _cityList
    }

    fun insert(cityList: CityList) = viewModelScope.launch(Dispatchers.IO) {
        cityListRepository.insert(cityList)
    }

    fun delete(cityList: CityList) = viewModelScope.launch(Dispatchers.IO) {
        cityListRepository.delete(cityList)
    }
}