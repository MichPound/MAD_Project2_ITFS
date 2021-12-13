package org.wit.itfs.models

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.itfs.helpers.exists
import org.wit.itfs.helpers.read
import org.wit.itfs.helpers.write
import java.lang.reflect.Type
import java.util.*

const val jsonFile = "tourSpots.json"
private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
private val listType = object : TypeToken<java.util.ArrayList<TourSpotModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class TourSpotJsonStore(private val context: Context) : TourSpotStore {

    var tourSpots = mutableListOf<TourSpotModel>()

    init {
        if (exists(context, jsonFile)) {
            deserialize()
        }
    }

    override fun create(tourSpot: TourSpotModel) {
        tourSpot.id = randID()
        tourSpots.add(tourSpot)
        serialize()
    }

    override fun list() {
        tourSpots.forEach {
            println(
                "ID: ${it.id}, Title: ${it.title}, County: ${it.county}, Description: ${it.desc}, " +
                        "Latitude: ${it.lat}, Longitude: ${it.long}, Contact info: ${it.contactInfo}, " +
                        "Open Time: ${it.openTime}, Closing Time: ${it.closingTime}, Ticket:  ${it.ticket}"
            )
        }
    }

    override fun update(updatedTourSpot: TourSpotModel) {
        val tourSpotList = findAll() as ArrayList<TourSpotModel>
        var tourSpot: TourSpotModel? = tourSpotList.find { p -> p.id == updatedTourSpot.id }
        if (tourSpot != null) {
            tourSpot.image = updatedTourSpot.image
            tourSpot.title = updatedTourSpot.title
            tourSpot.county = updatedTourSpot.county
            tourSpot.desc = updatedTourSpot.desc
            tourSpot.lat = updatedTourSpot.lat
            tourSpot.long = updatedTourSpot.long
            tourSpot.contactInfo = updatedTourSpot.contactInfo
            tourSpot.openTime = updatedTourSpot.openTime
            tourSpot.closingTime = updatedTourSpot.closingTime
            tourSpot.ticket = updatedTourSpot.ticket
        }
        serialize()
    }

    override fun delete(placemark: TourSpotModel) {
        tourSpots.remove(placemark)
        serialize()
    }

    override fun find(index: Long): TourSpotModel? {
        return tourSpots.find { p -> p.id == index }
    }

    override fun findAll(): MutableList<TourSpotModel> {
        return tourSpots
    }

    override fun findByTitle(title: String): TourSpotModel? {
        return tourSpots.find { p -> (p.title.lowercase()).contains(title.lowercase()) }
    }

    override fun amount(): Int {
        return tourSpots.size
    }

    override fun search(search: String): List<TourSpotModel> {
        val searchedList = ArrayList<TourSpotModel>()
        tourSpots.forEach { spot ->
            try {
                if (
                    (spot.title.lowercase()).contains(search.lowercase()) ||
                    (spot.county.lowercase()).contains(search.lowercase()) ||
                    (spot.desc.lowercase()).contains(search.lowercase()) ||
                    (spot.contactInfo.lowercase()).contains(search.lowercase()) ||
                    (spot.lat == search.toDouble()) ||
                    (spot.long == search.toDouble()) ||
                    (spot.openTime == search.toDouble()) ||
                    (spot.closingTime == search.toDouble())
                ) {
                    searchedList.add(spot)
                }
            } catch (e: Exception) {
            }
        }
        return searchedList
    }

    override fun countyFilter() {
        val counties = ArrayList<String>()
        tourSpots.forEach {
            if (!counties.contains("O. ${it.county.lowercase()}")) counties.add("O. ${it.county.lowercase()}")
        }

        var tempCounty: String
        do {
            counties.forEach {
                print("    $it")
            }

            println()
            println()

            val filteredList = ArrayList<TourSpotModel>()

            for (i in 0 until counties.size) {
                if (counties[i].startsWith("O")) {
                    tourSpots.forEach { spot ->
                        if ((spot.county.lowercase()).contains(counties[i].drop(3).lowercase())) {
                            filteredList.add(spot)
                        }
                    }
                }
            }
            filteredList.forEach {
                println(
                    "ID: ${it.id}, Title: ${it.title}, County: ${it.county}, Description: ${it.desc}, " +
                            "Latitude: ${it.lat}, Longitude: ${it.long}, Contact info: ${it.contactInfo}, " +
                            "Open Time: ${it.openTime}, Closing Time: ${it.closingTime}, Ticket:  ${it.ticket}"
                )
            }

            println()

            println("-1 to Exit")
            println("Enter name of county to add/remove from filtered list: ")
            tempCounty = readLine()!!

            for (i in 0 until counties.size) {
                if (counties[i].drop(3).lowercase() == (tempCounty.lowercase())) {
                    if (counties[i].startsWith("O")) {
                        counties[i] = "X${counties[i].drop(1)}"
                    } else {
                        counties[i] = "O${counties[i].drop(1)}"
                    }
                }
            }
        } while (tempCounty != "-1")
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(tourSpots, listType)
        write(context, jsonFile, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, jsonFile)
        tourSpots = gsonBuilder.fromJson(jsonString, listType)
    }
}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}