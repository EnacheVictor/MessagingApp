package com.example.messagingapp.presentation.screens.contact

import com.example.messagingapp.model.data.MessageEntity

data class ContactUiState(
    val searchText: String = "",
    val messages: List<MessageEntity> = emptyList(),
    val filteredMessages: List<MessageEntity> = emptyList()
)
