package org.wit.itfs.models

interface WeatherStore {
//    fun getWeather(tourSpot: TourSpotModel): WeatherModel
    fun getWeather(json: String?): WeatherModel
}