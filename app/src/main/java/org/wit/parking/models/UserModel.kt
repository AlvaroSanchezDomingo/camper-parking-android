package org.wit.parking.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var id: Long = 0,
                     var username: String = "",
                     var password: String = "") : Parcelable
