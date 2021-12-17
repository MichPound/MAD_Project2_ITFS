package org.wit.itfs.models

interface WeatherStore {
    fun getWeather(json: String?): WeatherModel
}