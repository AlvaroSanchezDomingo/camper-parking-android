package org.wit.parking.main


import android.app.Application
import org.wit.parking.models.ParkingMemStore
import org.wit.parking.models.UserMemStore
import org.wit.parking.models.UserModel
import timber.log.Timber
import timber.log.Timber.i

//https://developer.android.com/reference/android/app/Application
class MainApp : Application() {

    val parkings = ParkingMemStore()
    val users = UserMemStore()
    var loggedInUserId:Long? = null

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Placemark started")
    }
}