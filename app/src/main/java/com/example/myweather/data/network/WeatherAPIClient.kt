package com.example.myweather.data.network

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WeatherAPIClient {
    fun getClient(context: Context,url: String): WeatherAPIService{
        val cacheSize = (5 * 1024 * 1024).toLong() // 5MB
        val cache = Cache(context.cacheDir, cacheSize)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HeadInterceptor())
            .addNetworkInterceptor(HeadInterceptor())
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(WeatherAPIService::class.java)
    }
}