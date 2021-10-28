package org.wit.parking.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.parking.R
import org.wit.parking.databinding.ActivityParkingBinding
import org.wit.parking.helpers.showImagePicker
import org.wit.parking.main.MainApp
import androidx.activity.result.ActivityResultLauncher
import timber.log.Timber.i
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import org.wit.parking.models.Location
import org.wit.parking.models.ParkingModel
import timber.log.Timber


class ParkingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParkingBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var parking = ParkingModel()


    lateinit var app: MainApp

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            var location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            parking.lat = location.lat
                            parking.lng = location.lng
                            parking.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            parking.image = result.data!!.data!!
                            Picasso.get()
                                .load(parking.image)
                                .resize(500,500)
                                .into(binding.parkingImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerImagePickerCallback()
        registerMapCallback()

        var edit = false

        binding = ActivityParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Edit Parking"
        setSupportActionBar(binding.toolbar)


        app = application as MainApp
        binding.rating.minValue = 0
        binding.rating.maxValue = 5

        if (intent.hasExtra("parking_edit")) {
            edit = true
            parking = intent.extras?.getParcelable("parking_edit")!!
            binding.parkingTitle.setText(parking.title)
            binding.description.setText(parking.description)
            binding.rating.value = parking.rating

            Picasso.get()
                .load(parking.image)
                .resize(500,500)
                .into(binding.parkingImage)
            binding.btnAdd.setText(R.string.button_update)
            if (parking.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.button_changeImage)
            }
        }

        binding.btnAdd.setOnClickListener() {
            parking.title = binding.parkingTitle.text.toString()
            parking.description = binding.description.text.toString()
            parking.rating = binding.rating.value

            if (parking.title.isNotEmpty()) {
                if(edit){
                    app.parkings.update(parking.copy())
                }else{
                    parking.username = app.loggedInUser!!
                    i("Parking User: ${parking.username}")
                    app.parkings.create(parking.copy())
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,R.string.toast_enterTitle, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        binding.chooseImage.setOnClickListener() {
            showImagePicker(imageIntentLauncher)
        }
        binding.placemarkLocation.setOnClickListener {

            val location = Location(52.245696, -7.139102, 15f)
            if (parking.zoom != 0f) {
                location.lat =  parking.lat
                location.lng = parking.lng
                location.zoom = parking.zoom
            }

            val launcherIntent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_my_parking, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                app.parkings.delete(parking.copy())
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}