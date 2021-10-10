package org.wit.parking.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.parking.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_parking_image.toString())
    intentLauncher.launch(chooseFile)
}

