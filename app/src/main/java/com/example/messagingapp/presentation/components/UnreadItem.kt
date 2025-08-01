package com.example.messagingapp.presentation.components

data class UnreadItem(
    val username: String,
    val lastMessage: String,
    val timestamp: Long
)
