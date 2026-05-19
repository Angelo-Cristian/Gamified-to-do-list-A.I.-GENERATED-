package com.example.proiectkotlin.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "user_profile")
@Serializable
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val totalXp: Int = 0,
    val level: Int = 1
)
