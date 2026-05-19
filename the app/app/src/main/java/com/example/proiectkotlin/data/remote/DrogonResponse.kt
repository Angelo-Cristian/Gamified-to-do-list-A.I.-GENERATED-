package com.example.proiectkotlin.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class DrogonResponse(
    val status: String,
    val mesaj: String
)
