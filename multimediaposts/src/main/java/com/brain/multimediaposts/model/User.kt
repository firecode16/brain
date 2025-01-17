package com.brain.multimediaposts.model

import java.io.Serializable

data class User(
    var userId: Long,
    var userName: String,
    var fullName: String,
    var email: String,
    var phone: Long,
    var overview: String,
    var backdropImage: String,
    var countContacts: Int,
    var auth: Boolean = false
) : Serializable