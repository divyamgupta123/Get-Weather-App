package com.example.getweather.modal

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("lat")
    val lat:String,
    @SerializedName("lon")
    val lon:String,
    @SerializedName("timezone")
    var timezone: String,
    @SerializedName("current")
    var current: CurrentWeather,
    @SerializedName("daily")
    var daily: ArrayList<DailyWeather>
)