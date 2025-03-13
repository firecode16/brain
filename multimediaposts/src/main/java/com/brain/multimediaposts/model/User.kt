package com.brain.multimediaposts.model

import java.io.Serializable

data class User(
    var userId: Long,
    var userName: String,
    var fullName: String,
    var email: String,
    var phone: String,
    var registrationDate: String,
    var countContacts: Int,
    var auth: Boolean = false
) : Serializable