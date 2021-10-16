package org.wit.parking.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.parking.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "parking.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
                 .registerTypeAdapter(Uri::class.java, UriParser())
                 .create()
val listType: Type = object : TypeToken<ArrayList<ParkingModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ParkingJSONStore(private val context: Context) : ParkingStore {

    var parkings = mutableListOf<ParkingModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<ParkingModel> {
        logAll()
        return parkings
    }

    override fun create(placemark: ParkingModel) {
        placemark.id = generateRandomId()
        parkings.add(placemark)
        serialize()
    }


    override fun update(placemark: ParkingModel) {
        val parkingsList = findAll() as ArrayList<ParkingModel>
        var foundParking: ParkingModel? = parkingsList.find { p -> p.id == placemark.id }
        if (foundParking != null) {
            foundParking.title = placemark.title
            foundParking.description = placemark.description
            foundParking.image = placemark.image
            foundParking.lat = placemark.lat
            foundParking.lng = placemark.lng
            foundParking.zoom = placemark.zoom
        }
        serialize()
    }

    override fun delete(placemark: ParkingModel) {
        parkings.remove(placemark)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(parkings, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        parkings = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        parkings.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
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