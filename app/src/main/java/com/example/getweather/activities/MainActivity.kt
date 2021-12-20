package com.example.getweather.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getweather.Constants
import com.example.getweather.R
import com.example.getweather.adapters.DailyWeatherAdapter
import com.example.getweather.databinding.ActivityMainBinding
import com.example.getweather.modal.DailyWeather
import com.example.getweather.retrofitnetwork.ApiInterface
import com.example.getweather.retrofitnetwork.WeatherRepository
import com.example.getweather.utilities.Utility
import com.example.getweather.viewmodel.WeatherViewModel
import com.example.getweather.viewmodel.WeatherViewModelFactory
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var search: MenuItem
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    lateinit var viewModel: WeatherViewModel
    private val apiInterface = ApiInterface.create()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: WeatherViewModelFactory =
            WeatherViewModelFactory(WeatherRepository(apiInterface),this@MainActivity)

        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
        if (isNetworkAvailable(this)) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            binding.noInternet.visibility = View.GONE
            binding.pgView.visibility = View.VISIBLE
            binding.scrollView2.visibility = View.GONE
            supportActionBar?.show()
            getLastLocation()

        } else {
            binding.noInternet.visibility = View.VISIBLE
            binding.pgView.visibility = View.GONE
            binding.scrollView2.visibility = View.GONE
            supportActionBar?.hide()
        }
    }

    private fun getLatLong(cityName: String) {
        if (Geocoder.isPresent()) {
            try {
                val geocoder = Geocoder(this)
                val addresses: List<Address> =
                    geocoder.getFromLocationName(cityName, 1)

                if (addresses.isEmpty()) {
                    Toast.makeText(this, "Please enter correct location", Toast.LENGTH_SHORT).show()
                } else {
                    val latitude = addresses[0].latitude.toString()
                    val longitude = addresses[0].longitude.toString()
                    viewModel.updateState(cityName, latitude, longitude)
                    currentWeather()
                }

            } catch (e: IOException) {

            }
        }
    }

    private fun currentWeather() {
        val rvDailyWeather = binding.rvDailyWeather
        rvDailyWeather.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.getWeather().observe(this, Observer { res ->
            binding.noInternet.visibility = View.GONE
            binding.pgView.visibility = View.GONE
            binding.scrollView2.visibility = View.VISIBLE

            val utility: Utility = Utility()

            val city = viewModel.cityName

            binding.cityName.text =
                getString(R.string.cityName, city!!.replaceFirstChar { it.uppercase() })

            binding.latitudeText.text = res.lat
            binding.longitudeText.text = res.lon
            binding.timezoneText.text = res.timezone
            binding.temperature.text = getString(
                R.string.temperature,
                ceil(res.current.temp.toDouble()).toInt().toString()
            )
            binding.windText.text =
                getString(R.string.windSpeed, utility.speedConvert(res.current.windSpeed))
            binding.description.text = res.current.weather[0].description
            binding.main.text = res.current.weather[0].main

            binding.sunriseTime.text =
                getString(R.string.morningTime, utility.getTime(res.current.sunrise))
            binding.sunsetTime.text =
                getString(R.string.eveningTime, utility.getTime(res.current.sunset))
            binding.humidityText.text = getString(R.string.humidity, res.current.humidity)
            binding.pressureText.text = getString(R.string.pressure, res.current.pressure)
            binding.uviText.text = res.current.uvi

            utility.loadImage(
                this@MainActivity,
                res.current.weather[0].icon,
                binding.imageView
            )

            val adapter = DailyWeatherAdapter(
                this@MainActivity,
                res.daily.drop(1) as ArrayList<DailyWeather>
            )
            rvDailyWeather.adapter = adapter
        })
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        // check if permissions are given

        if (viewModel.cityName != null) {
            currentWeather()
        } else {
            if (checkPermissions()) {

                // check if location is enabled
                if (isLocationEnabled()) {
                    // getting last location from FusedLocationClient object
                    mFusedLocationClient.lastLocation
                        .addOnCompleteListener { task ->
                            val location: Location = task.result!!
                            val latitude = location.latitude
                            val longitude = location.longitude
                            val cityName = Utility().getCityName(latitude, longitude, this)

                            viewModel.updateState(
                                cityName,
                                latitude.toString(),
                                longitude.toString()
                            )
                            currentWeather()
                        }
                } else {
                    Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                // if permissions aren't available,
                // request for permissions
                requestPermissions()
            }
        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), Constants.LOCATION_PERMISSION_ID
        )
    }

    // method to check if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        // For below 29 api
        else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        search = menu?.findItem(R.id.menu_search)!!
        val searchView = search.actionView as SearchView

        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Enter City Name"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Please Enter City Name",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        search.collapseActionView()
                        searchView.onActionViewCollapsed()
                        getLatLong(query)

                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.myLocation -> {
                viewModel.updateState(null, "", "")
                getLastLocation()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

