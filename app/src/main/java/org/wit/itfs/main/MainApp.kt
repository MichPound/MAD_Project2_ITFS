package org.wit.itfs.main

import android.app.Application
import org.wit.itfs.models.TourSpotJsonStore
import org.wit.itfs.models.TourSpotStore
import org.wit.itfs.models.WeatherMemStore
import org.wit.itfs.models.WeatherStore

class MainApp() : Application() {

    lateinit var tourSpots: TourSpotStore
    lateinit var weather: WeatherStore

    override fun onCreate() {
        super.onCreate()
        tourSpots = TourSpotJsonStore(applicationContext)
        weather = WeatherMemStore()
    }
}