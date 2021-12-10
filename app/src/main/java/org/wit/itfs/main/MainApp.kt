package org.wit.itfs.main

import android.app.Application
import org.wit.itfs.models.TourSpotMemStore
import org.wit.itfs.models.TourSpotModel
import java.util.ArrayList

class MainApp() : Application() {

    val tourSpots = TourSpotMemStore()

    override fun onCreate() {
        super.onCreate()
    }
}