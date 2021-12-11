package org.wit.itfs.main

import android.app.Application
import org.wit.itfs.models.TourSpotMemStore
import org.wit.itfs.models.TourSpotModel
import org.wit.itfs.models.WeatherMemStore
import org.wit.itfs.models.WeatherStore
import java.util.ArrayList

class MainApp() : Application() {

    val tourSpots = TourSpotMemStore()
    val weather = WeatherMemStore()

    override fun onCreate() {
        super.onCreate()
    }
}