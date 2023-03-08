package com.example.myweather.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityListDao {
    @Query("SELECT * FROM Cities ORDER BY city ASC")
    fun getAll(): LiveData<List<CityList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cityList: CityList)

    @Delete
    fun delete(cityList: CityList)
}