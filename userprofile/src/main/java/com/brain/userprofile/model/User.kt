package com.brain.userprofile.model

import android.net.Uri
import java.io.Serializable

data class User(
    var userId: Long,
    var userName: String,
    var fullName: String,
    var email: String,
    var phone: String,
    var avatarProfile: Uri?,
    var backdropProfile: Uri?,
    var occupation: String,
    var registrationDate: String,
    var countContacts: Int,
    var auth: Boolean = false
) : Serializable