package com.example.getweather.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.getweather.Constants
import com.example.getweather.R
import com.example.getweather.databinding.ActivityDailyWeatherBinding
import com.example.getweather.modal.DailyWeather
import com.example.getweather.utilities.Utility
import kotlin.math.ceil

class DailyWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDailyWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dailyWeather = intent.getParcelableExtra<DailyWeather>(Constants.DAILY_WEATHER_INTENT)
        if (dailyWeather != null) {
            val utility = Utility()

            binding.date.text = utility.getDate(dailyWeather.dateTime)
            binding.temperature.text = getString(
                R.string.temperature,
                ceil(dailyWeather.dailyTemp.dayTemp.toDouble()).toInt().toString()
            )
            binding.main.text = dailyWeather.weather[0].main
            binding.description.text = dailyWeather.weather[0].description
            binding.pressureText.text = getString(R.string.pressure, dailyWeather.pressure)
            binding.humidityText.text = getString(R.string.humidity, dailyWeather.humidity)

            binding.sunriseTime.text =
                getString(R.string.morningTime, utility.getTime(dailyWeather.sunrise))
            binding.sunsetTime.text =
                getString(R.string.eveningTime, utility.getTime(dailyWeather.sunset))
            binding.moonriseText.text =
                getString(R.string.eveningTime, utility.getTime(dailyWeather.moonrise))
            binding.moonsetText.text =
                getString(R.string.morningTime, utility.getTime(dailyWeather.moonset))
            binding.windText.text =
                getString(R.string.windSpeed, utility.speedConvert(dailyWeather.windSpeed))
            binding.uviText.text = dailyWeather.uvi
            binding.maxTempText.text = getString(
                R.string.temperature,
                ceil(dailyWeather.dailyTemp.maxTemp.toDouble()).toInt().toString()
            )
            binding.minTempText.text = getString(
                R.string.temperature,
                ceil(dailyWeather.dailyTemp.minTemp.toDouble()).toInt().toString()
            )
            binding.mornTempText.text = getString(
                R.string.temperature,
                ceil(dailyWeather.dailyTemp.mornTemp.toDouble()).toInt().toString()
            )
            binding.nightTempText.text = getString(
                R.string.temperature,
                ceil(dailyWeather.dailyTemp.nightTemp.toDouble()).toInt().toString()
            )

            utility.loadImage(
                this@DailyWeatherActivity,
                dailyWeather.weather[0].icon,
                binding.imageView
            )
        }
    }
}

