package com.example.proiectkotlin.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class UserStats(
    val nume: String,
    val xp: Int,
    val level: Int,
    val tasks: List<String>
)
