package com.example.getweather.modal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeather(
    @SerializedName("dt")
    val dateTime: String,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("moonrise")
    val moonrise: String,
    @SerializedName("moonset")
    val moonset: String,
    @SerializedName("temp")
    val dailyTemp: DailyTemp,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("wind_speed")
    val windSpeed: String,
    @SerializedName("uvi")
    val uvi: String,
    @SerializedName("weather")
    val weather: ArrayList<Weather>
):Parcelable