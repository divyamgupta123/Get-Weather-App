package com.example.getweather.modal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyTemp(
    @SerializedName("day")
    val dayTemp:String,
    @SerializedName("morn")
    val mornTemp:String,
    @SerializedName("min")
    val minTemp:String,
    @SerializedName("max")
    val maxTemp:String,
    @SerializedName("night")
    val nightTemp:String,
) : Parcelable