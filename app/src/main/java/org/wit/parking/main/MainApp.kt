package org.wit.parking.main


import android.app.Application
import org.wit.parking.models.ParkingJSONStore
import org.wit.parking.models.ParkingStore
import org.wit.parking.models.UserMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var parkings : ParkingStore
    val users = UserMemStore()
    var loggedInUserId:Long? = null

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        parkings = ParkingJSONStore(applicationContext)
        i("Placemark started")
    }
}