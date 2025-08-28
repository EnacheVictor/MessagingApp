package com.example.messagingapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val senderUsername: String,
    val receiverUsername: String,
    val messageText: String,
    val timestamp: Long,
    val isRead: Int = 1
)