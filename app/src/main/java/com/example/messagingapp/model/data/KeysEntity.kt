package com.example.messagingapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_table")
data class KeysEntity(
    @PrimaryKey val id: Int = 0,
    val privateKey: String
)
