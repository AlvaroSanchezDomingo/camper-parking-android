package org.wit.parking.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import org.wit.parking.R
import org.wit.parking.databinding.ActivityViewParkingBinding
import org.wit.parking.main.MainApp
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import org.wit.parking.databinding.ContentParkingMapsBinding
import org.wit.parking.databinding.ContentParkingMapsSimpleBinding
import org.wit.parking.models.ParkingModel



class ViewParkingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewParkingBinding
    private lateinit var contentBinding: ContentParkingMapsSimpleBinding
    lateinit var map: GoogleMap

    var parking = ParkingModel()


    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Parking View"
        setSupportActionBar(binding.toolbar)


        app = application as MainApp

        if (intent.hasExtra("parking_view")) {
            parking = intent.extras?.getParcelable("parking_view")!!
            binding.parkingTitle.setText(parking.title)
            binding.description.setText(parking.description)
            binding.rating.setText(parking.rating.toString())
            Picasso.get()
                .load(parking.image)
                .resize(500, 500)
                .into(binding.parkingImage)

            contentBinding = ContentParkingMapsSimpleBinding.bind(binding.root)
            contentBinding.mapView.onCreate(savedInstanceState)
            contentBinding.mapView.getMapAsync {
                map = it
                configureMap()
            }
        }
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        val loc = LatLng(parking.lat, parking.lng)
        val options = MarkerOptions().title(parking.title).position(loc)
        map.addMarker(options).tag = parking.id
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, parking.zoom))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_parking, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}


