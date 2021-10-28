package org.wit.parking.models

interface ParkingStore {
    fun findAll(): List<ParkingModel>
    fun findParkingById(id:Long): ParkingModel?
    fun findParkingByUsername(username:String): List<ParkingModel>?
    fun create(parking: ParkingModel)
    fun update(parking: ParkingModel)
    fun delete(parking: ParkingModel)

    fun findByUsername(username: String) : UserModel?
    fun create(user: UserModel):Boolean
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun authenticate(user: UserModel) : UserModel?
}
