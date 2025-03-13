package com.brain.model

/**
 * @author brain30316@gmail.com
 *
 */
data class RegisterRequest(
    val userId: Long,
    val email: String,
    val username: String,
    val password: String,
    val fullName: String,
    val phone: String,
    val registrationDate: String
)
