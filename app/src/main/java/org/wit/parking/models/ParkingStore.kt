package org.wit.parking.models

interface ParkingStore {
    fun findAll(): List<ParkingModel>
    fun create(parking: ParkingModel)
    fun update(parking: ParkingModel)
    fun delete(parking: ParkingModel)
}