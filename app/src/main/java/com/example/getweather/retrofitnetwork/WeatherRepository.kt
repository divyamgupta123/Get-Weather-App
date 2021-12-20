package com.example.getweather.retrofitnetwork

class WeatherRepository(
    private val apiInterface: ApiInterface
) {
    fun getWeather(
        latitude: String,
        longitude: String,
        apiId: String
    ) = apiInterface.getWeatherData(latitude, longitude, apiId)
}