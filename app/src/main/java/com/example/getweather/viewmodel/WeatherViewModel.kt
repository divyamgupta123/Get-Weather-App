package com.example.getweather.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getweather.R
import com.example.getweather.modal.WeatherResponse
import com.example.getweather.retrofitnetwork.WeatherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val context: Context
) : ViewModel() {
    val weatherData = MutableLiveData<WeatherResponse>()
    lateinit var latitude: String
    lateinit var longitude: String

    var cityName: String? = null

    fun getWeather(): LiveData<WeatherResponse> {
        loadWeather()
        return weatherData
    }

    private fun loadWeather() {

        val response =
            weatherRepository.getWeather(latitude, longitude, context.getString(R.string.apiId))

        response.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                weatherData.value = response.body()!!
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            }
        })
    }

    fun updateState(city: String?, latitude: String, longitude: String) {
        cityName = city
        this.latitude = latitude
        this.longitude = longitude
    }
}