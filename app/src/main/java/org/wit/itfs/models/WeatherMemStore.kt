package org.wit.itfs.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val listType = object : TypeToken<WeatherModel>() {}.type

class WeatherMemStore : WeatherStore {

    override fun getWeather(json: String?): WeatherModel {
        return Gson().fromJson(json, listType)
    }

}