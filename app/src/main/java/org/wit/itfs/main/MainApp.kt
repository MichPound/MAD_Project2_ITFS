package org.wit.itfs.main

import android.app.Application
import org.wit.itfs.models.*
import java.util.ArrayList

class MainApp() : Application() {

    lateinit var tourSpots:TourSpotStore
    lateinit var weather:WeatherStore

    override fun onCreate() {
        super.onCreate()
//        tourSpots = TourSpotMemStore()
        tourSpots = TourSpotJsonStore(applicationContext)
        weather = WeatherMemStore()
    }
}