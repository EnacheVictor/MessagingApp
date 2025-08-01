package com.example.messagingapp.presentation.screens.unread

sealed class UnreadUiEvent {
    data class Init(val loggedInUsername: String) : UnreadUiEvent()
    data class SearchChanged(val text: String) : UnreadUiEvent()
    data class StatusSelected(val status: String) : UnreadUiEvent()
}