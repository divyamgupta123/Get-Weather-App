package com.example.getweather.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getweather.Constants
import com.example.getweather.R
import com.example.getweather.activities.DailyWeatherActivity
import com.example.getweather.databinding.DailyWeatherItemLayoutBinding
import com.example.getweather.modal.DailyWeather
import com.example.getweather.utilities.Utility
import kotlin.math.ceil

class DailyWeatherAdapter(private val context: Context, private val list: ArrayList<DailyWeather>) :
    RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DailyWeatherItemLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DailyWeatherActivity::class.java)
            intent.putExtra(Constants.DAILY_WEATHER_INTENT, item)
            context.startActivity(intent)

        }

        holder.temp.text = ceil(item.dailyTemp.dayTemp.toDouble()).toString()
        holder.weather.text = item.weather[0].main
        holder.date.text = Utility().getDate(item.dateTime)
        holder.windSpeed.text =
            context.getString(R.string.windSpeed, Utility().speedConvert(item.windSpeed))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: DailyWeatherItemLayoutBinding) :
        RecyclerView.ViewHolder(ItemView.root) {

        val temp = ItemView.dailyTemperature
        val date = ItemView.date
        val windSpeed = ItemView.dailyWindSpeed
        val weather = ItemView.dailyWeather
    }
}