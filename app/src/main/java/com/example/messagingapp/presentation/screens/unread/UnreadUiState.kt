package com.example.messagingapp.presentation.screens.unread

import com.example.messagingapp.presentation.components.UnreadItem

data class UnreadUiState(
    val loggedInUsername: String = "",
    val searchText: String = "",
    val unreadItems: List<UnreadItem> = emptyList(),
    val filteredUnreadItems: List<UnreadItem> = emptyList()
)
