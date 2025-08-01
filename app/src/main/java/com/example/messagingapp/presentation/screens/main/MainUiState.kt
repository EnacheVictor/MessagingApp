package com.example.messagingapp.presentation.screens.main

import com.example.messagingapp.model.data.UserEntity

data class MainUiState(
    val currentUsername: String = "",
    val searchText: String = "",
    val allContacts: List<UserEntity> = emptyList(),
    val filteredContacts: List<UserEntity> = emptyList(),
    val selectedStatus: String = "😎 Available",
    val showDeleteDialog: Boolean = false,
    val contactToDelete: String? = null
)
