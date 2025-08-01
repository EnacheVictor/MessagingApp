package com.example.messagingapp.presentation.screens.favorites

sealed class FavoritesUiEvent {
    data class Init(val username: String) : FavoritesUiEvent()
    data class SearchChanged(val text: String) : FavoritesUiEvent()
    data class RemoveFavorite(val username: String) : FavoritesUiEvent()
    data class StatusSelected(val status: String) : FavoritesUiEvent()
}