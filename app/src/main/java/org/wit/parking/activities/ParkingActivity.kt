package org.wit.parking.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.parking.R
import org.wit.parking.databinding.ActivityParkingBinding
import org.wit.parking.models.ParkingModel
import org.wit.parking.helpers.showImagePicker
import org.wit.parking.main.MainApp
import androidx.activity.result.ActivityResultLauncher
import timber.log.Timber.i
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso


//https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity
class ParkingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParkingBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var parking = ParkingModel()
    lateinit var app: MainApp

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
                                .into(binding.placemarkImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerImagePickerCallback()

        var edit = false

        binding = ActivityParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)


        app = application as MainApp

        if (intent.hasExtra("parking_edit")) {
            edit = true
            parking = intent.extras?.getParcelable("parking_edit")!!
            binding.placemarkTitle.setText(parking.title)
            binding.description.setText(parking.description)
            Picasso.get()
                .load(parking.image)
                .into(binding.placemarkImage)
            binding.btnAdd.setText(R.string.button_savePlacemark)
            binding.chooseImage.setText(R.string.button_changeImage)
        }

        binding.btnAdd.setOnClickListener() {
            parking.title = binding.placemarkTitle.text.toString()
            parking.description = binding.description.text.toString()
            if (parking.title.isNotEmpty()) {
                if(edit){
                    app.parkings.update(parking.copy())
                }else{
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
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
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
}