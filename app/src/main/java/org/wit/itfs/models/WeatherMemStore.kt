package org.wit.itfs.models

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.os.AsyncTask
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.wit.itfs.APIKEY
import java.net.URL

import com.android.volley.RequestQueue

import android.widget.Toast
import androidx.core.net.toUri
import com.android.volley.Response

import org.wit.itfs.activities.MainActivity

import com.android.volley.VolleyError
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

private val listType = object : TypeToken<WeatherModel>() {}.type

class WeatherMemStore : WeatherStore {

    override fun getWeather(json: String?): WeatherModel {
        return Gson().fromJson(json, listType)
    }

}