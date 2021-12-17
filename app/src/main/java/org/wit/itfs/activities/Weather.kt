package org.wit.itfs.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.wit.itfs.APIKEY
import org.wit.itfs.R
import org.wit.itfs.databinding.ActivityWeatherBinding
import org.wit.itfs.main.MainApp
import org.wit.itfs.models.TourSpotModel
import org.wit.itfs.models.WeatherModel
import java.net.URL
import kotlin.math.roundToInt

class Weather : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    lateinit var app: MainApp
    var tourSpot = TourSpotModel()
    lateinit var currentWeather: WeatherModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        weatherProcess().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_update -> {
                tourSpot = intent.extras?.getParcelable("tourSpot_edit")!!
                val launcherIntent = Intent(this, MainActivity::class.java)
                launcherIntent.putExtra("tourSpot_edit", tourSpot)
                startActivityForResult(launcherIntent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class weatherProcess() : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {

            tourSpot = intent.extras?.getParcelable("tourSpot_edit")!!

            val lat = tourSpot.lat
            val long = tourSpot.long

            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&appid=$APIKEY").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }

            return response
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            currentWeather = app.weather.getWeather(result)

            val response =
                URL("https://openweathermap.org/img/w/${currentWeather.weather[0].icon}.png")
            Glide.with(applicationContext).load(response).into(binding.tourSpotImage)

            binding.weatherName.text = tourSpot.title
            binding.weatherMain.text = currentWeather.weather[0].main
            binding.weatherDesc.text = "Description: " + currentWeather.weather[0].description
            binding.weatherTemp.text =
                (currentWeather.main.temp - 273.15).roundToInt().toString() + "\u2103"
            binding.weatherFeel.text =
                "Feels Like: " + (currentWeather.main.feels_like - 273.15).roundToInt()
                    .toString() + "\u2103"
            binding.weatherPressure.text = "Pressure: " + currentWeather.main.pressure.toString()
            binding.weatherHumidity.text = "Humidity: " + currentWeather.main.humidity.toString()
            binding.weatherSpeed.text = "Wind Speed: " + currentWeather.wind.speed.toString()
            binding.weatherVis.text = "Visibility: " + currentWeather.visibility.toString()
            binding.weatherCloud.text = "Cloud: " + currentWeather.clouds.all.toString()
            binding.weatherSunrise.text = "Sunrise: " + currentWeather.sys.sunrise.toString()
            binding.weatherSunset.text = "Sunset: " + currentWeather.sys.sunset.toString()
        }
    }
}