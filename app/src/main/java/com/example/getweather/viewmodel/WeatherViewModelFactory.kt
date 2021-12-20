package com.example.getweather.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.getweather.retrofitnetwork.WeatherRepository

class WeatherViewModelFactory(private val weatherRepository: WeatherRepository,private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherRepository,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}