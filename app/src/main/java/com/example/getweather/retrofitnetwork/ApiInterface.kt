package com.example.getweather.retrofitnetwork

import com.example.getweather.modal.WeatherResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("data/2.5/onecall?exclude=minutely,hourly&units=metric&")
    fun getWeatherData(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") apiId:String
    ): Call<WeatherResponse>

    companion object{
        private var BASE_URL = "https://api.openweathermap.org/"

        fun create():ApiInterface{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }

    }
}