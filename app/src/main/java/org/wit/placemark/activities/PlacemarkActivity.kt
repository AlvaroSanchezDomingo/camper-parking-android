package org.wit.placemark.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.models.PlacemarkModel
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.main.MainApp
import androidx.activity.result.ActivityResultLauncher
import timber.log.Timber.i
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso


class PlacemarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var placemark = PlacemarkModel()
    lateinit var app: MainApp

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            placemark.image = result.data!!.data!!
                            Picasso.get()
                                .load(placemark.image)
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

        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)


        app = application as MainApp

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = intent.extras?.getParcelable("placemark_edit")!!
            binding.placemarkTitle.setText(placemark.title)
            binding.description.setText(placemark.description)
            Picasso.get()
                .load(placemark.image)
                .into(binding.placemarkImage)
            binding.btnAdd.setText(R.string.button_savePlacemark)
            binding.chooseImage.setText(R.string.button_changeImage)
        }

        binding.btnAdd.setOnClickListener() {
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.description.text.toString()
            if (placemark.title.isNotEmpty()) {
                if(edit){
                    app.placemarks.update(placemark.copy())
                }else{
                    app.placemarks.create(placemark.copy())
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