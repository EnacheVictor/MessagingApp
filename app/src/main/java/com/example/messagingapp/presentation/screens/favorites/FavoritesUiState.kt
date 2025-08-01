package com.example.messagingapp.presentation.screens.favorites

import com.example.messagingapp.presentation.components.FavoritesItem

data class FavoritesUiState(
    val loggedInUsername: String = "",
    val searchText: String = "",
    val favorites: List<FavoritesItem> = emptyList(),
    val filteredFavorites: List<FavoritesItem> = emptyList()
)
