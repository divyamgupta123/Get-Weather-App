package com.example.getweather.modal

import com.google.gson.annotations.SerializedName

data class CurrentWeather (
    @SerializedName("dt")
    val dateTime: String,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("temp")
    val temp: String,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("wind_speed")
    val windSpeed: String,
    @SerializedName("uvi")
    val uvi: String,
    @SerializedName("clouds")
    val clouds: String,
    @SerializedName("weather")
    val weather: ArrayList<Weather>
)