package com.example.messagingapp.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val username: String,
    val status: String = "Available",
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false
)