package org.wit.parking.main


import android.app.Application
import org.wit.parking.models.ParkingJSONStore
import org.wit.parking.models.ParkingStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var parkings : ParkingStore
    var loggedInUser:String? = null

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        parkings = ParkingJSONStore(applicationContext)
        i("Placemark started")
    }
}