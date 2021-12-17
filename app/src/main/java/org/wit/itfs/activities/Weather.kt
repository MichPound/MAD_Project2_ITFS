package org.wit.itfs.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.core.net.toUri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import okio.Timeout
import org.json.JSONObject
import org.wit.itfs.APIKEY
import org.wit.itfs.R
import org.wit.itfs.databinding.ActivityMainBinding
import org.wit.itfs.databinding.ActivityWeatherBinding
import org.wit.itfs.helpers.showImagePicker
import org.wit.itfs.main.MainApp
import org.wit.itfs.models.TourSpotModel
import org.wit.itfs.models.WeatherModel
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
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

        weatherTask().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
            R.id.item_update -> {
                tourSpot = intent.extras?.getParcelable("tourSpot_edit")!!
                val launcherIntent = Intent(this, MainActivity::class.java)
                launcherIntent.putExtra("tourSpot_edit", tourSpot)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //    https://github.com/evanemran/WeatherApp/blob/master/app/src/main/java/com/example/weatherapp/MainActivity.kt
    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {

            tourSpot = intent.extras?.getParcelable("tourSpot_edit")!!

            val lat = tourSpot.lat

            val long = tourSpot.long

            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&appid=$APIKEY").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }

            return response
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            currentWeather = app.weather.getWeather(result)

            val response = URL("https://openweathermap.org/img/w/${currentWeather.weather[0].icon}.png")
            Glide.with(applicationContext).load(response).into(binding.tourSpotImage)

            binding.weatherName.text = tourSpot.title
            binding.weatherMain.text = currentWeather.weather[0].main
            binding.weatherDesc.text = currentWeather.weather[0].description
            binding.weatherTemp.text = (currentWeather.main.temp - 273.15).roundToInt().toString() + "\u2103"
            binding.weatherFeel.text = (currentWeather.main.feels_like - 273.15).roundToInt().toString() + "\u2103"
            binding.weatherMin.text = (currentWeather.main.temp_min - 273.15).roundToInt().toString() + "\u2103"
            binding.weatherMax.text = (currentWeather.main.temp_max - 273.15).roundToInt().toString() + "\u2103"
            binding.weatherPressure.text = currentWeather.main.pressure.toString()
            binding.weatherHumidity.text = currentWeather.main.humidity.toString()
            binding.weatherSpeed.text = currentWeather.wind.speed.toString()
            binding.weatherDeg.text = currentWeather.wind.deg.toString()
            binding.weatherGust.text = currentWeather.wind.gust.toString()
            binding.weatherVis.text = currentWeather.visibility.toString()
            binding.weatherCloud.text = currentWeather.clouds.all.toString()
            binding.weatherSunrise.text = currentWeather.sys.sunrise.toString()
            binding.weatherSunset.text = currentWeather.sys.sunset.toString()
        }
    }

}