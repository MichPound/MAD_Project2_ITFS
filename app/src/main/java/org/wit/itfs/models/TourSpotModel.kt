package org.wit.itfs.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TourSpotModel(
    var id: Long = 0,
    var image: String = "",
    var title: String = "",
    var county: String = "",
    var desc: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var contactInfo: String = "N/A",
    var openTime: Double = 00.00,
    var closingTime: Double = 00.00,
    var ticket: Boolean = false
) : Parcelable
