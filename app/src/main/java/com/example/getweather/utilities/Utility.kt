package com.example.getweather.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Utility {

    @SuppressLint("SimpleDateFormat")
    fun getDate(date: String): String {
        val sdfDate = SimpleDateFormat("dd")
        val dt = Date(date.toLong() * 1000)
        val dateOrig = sdfDate.format(dt)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.toLong() * 1000
        val month =
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)!!.toString()

        return "$dateOrig $month"
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(time: String): String {
        val sdf = SimpleDateFormat("hh:mm")
        val date = Date(time.toLong() * 1000)
        return sdf.format(date)
    }

    fun loadImage(context: Context, icon: String, imageView: ImageView) {
        val url = "https://openweathermap.org/img/wn/$icon@4x.png"
        Glide.with(context)
            .load(url) // image url
            .centerCrop()
            .into(imageView)
    }

    fun getCityName(latitude: Double, longitude: Double, context: Context): String {

        val geocoder = Geocoder(context)
        val addresses: List<Address>?
        var city = "no"
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (null != addresses && addresses.isNotEmpty()) { //prevent from error
                //sometimes the city comes in locality, sometimes in subAdminArea.
                city = if (addresses[0].locality == null) {
                    addresses[0].subAdminArea
                } else {
                    addresses[0].locality
                }
            }
        } catch (e: IOException) {

        }
        return city
    }

    fun speedConvert(speed: String): String {
        val decimalFormat = DecimalFormat("#.##")
        val result = speed.toDouble() * 3.6
        return decimalFormat.format(result)
    }

}