package org.wit.itfs.models

import android.app.DownloadManager
import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.wit.itfs.APIKEY
import java.net.URL

import com.android.volley.RequestQueue

import android.widget.Toast
import com.android.volley.Response

import org.wit.itfs.activities.MainActivity

import com.android.volley.VolleyError

private val listType = object : TypeToken<WeatherModel>() {}.type

class WeatherMemStore : WeatherStore {
    override fun getWeather(json: String?): WeatherModel {
        val lat = 52.26
        val long = -7.12
        val apikey = APIKEY

//        var json = ""
//        try {
////            val queue = Volley.newRequestQueue(this)
//            val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&appid=$apikey"
//
//            val stringRequest = StringRequest(Request.Method.POST, url,
//                { response ->
//                    json = response
//                },
//                { json = "That didn't work!" })
//
////            json = stringRequest("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&appid=$apikey")
////                .readText()
//        } catch (e: Exception) {
//            println(e)
//        }

        return Gson().fromJson(json, listType)
    }
}