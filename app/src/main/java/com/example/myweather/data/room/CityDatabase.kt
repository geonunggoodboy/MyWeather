package com.example.myweather.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityList::class], version = 1)
abstract class CityDatabase: RoomDatabase() {
    abstract fun cityListDao(): CityListDao

    companion object {
        private var instance: CityDatabase? = null

        fun getDatabase(
            context:Context
        ): CityDatabase? {
            if(instance == null)
                synchronized(CityDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CityDatabase::class.java,
                        "CityList.db"
                    )
                        .build()
                }
                return instance
            }
        }
}