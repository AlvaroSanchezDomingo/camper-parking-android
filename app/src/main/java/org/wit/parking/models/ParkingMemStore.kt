package org.wit.parking.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ParkingMemStore : ParkingStore {

    val parkings = ArrayList<ParkingModel>()

    override fun findAll(): List<ParkingModel> {
        return parkings
    }

    override fun create(parking: ParkingModel) {
        parking.id = getId()
        parkings.add(parking)
        logAll()
    }
    override fun update(parking: ParkingModel) {
        var foundParking: ParkingModel? = parkings.find { p -> p.id == parking.id }
        if (foundParking != null) {
            foundParking.title = parking.title
            foundParking.description = parking.description
            foundParking.image = parking.image
            foundParking.lat = parking.lat
            foundParking.lng = parking.lng
            foundParking.zoom = parking.zoom
            logAll()
        }
    }
    override fun delete(parking: ParkingModel) {
        var foundParking: ParkingModel? = parkings.find { p -> p.id == parking.id }
        if (foundParking != null) {
            parkings.removeAt(parkings.indexOf(foundParking))
            logAll()
        }
    }
    fun logAll() {
        parkings.forEach{ i("${it}") }
    }
}