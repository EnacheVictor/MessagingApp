package com.example.messagingapp.presentation.screens.main

sealed class MainUiEvent {
    data class Init(val loggedInUsername: String) : MainUiEvent()
    data class SearchChanged(val text: String) : MainUiEvent()
    data class ToggleFavorite(val username: String) : MainUiEvent()
    data class PromptDelete(val username: String) : MainUiEvent()
    object ConfirmDelete : MainUiEvent()
    object CancelDelete : MainUiEvent()
    data class StatusSelected(val status: String) : MainUiEvent()
}