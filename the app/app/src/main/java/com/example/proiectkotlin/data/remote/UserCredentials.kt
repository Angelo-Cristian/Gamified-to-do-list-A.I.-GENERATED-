package com.example.proiectkotlin.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val email: String,
    val parola: String,
    val nume: String = "",
    val xp: Int = 0,
    val level: Int = 1
)
