package com.example.messagingapp.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.presentation.components.FavoritesItem
import com.example.messagingapp.repository.MessageRepository
import com.example.messagingapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState

    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            is FavoritesUiEvent.Init -> {
                _uiState.update { it.copy(loggedInUsername = event.username) }
                loadFavorites()
            }

            is FavoritesUiEvent.SearchChanged -> {
                val filtered = filter(_uiState.value.favorites, event.text)
                _uiState.update { it.copy(searchText = event.text, filteredFavorites = filtered) }
            }

            is FavoritesUiEvent.RemoveFavorite -> {
                viewModelScope.launch {
                    userRepository.setFavorite(event.username, false)
                    loadFavorites()
                }
            }

            is FavoritesUiEvent.StatusSelected -> {
                viewModelScope.launch {
                    userRepository.updateStatus(_uiState.value.loggedInUsername, event.status)
                }
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val user = _uiState.value.loggedInUsername
            val favorites = userRepository.getFavorites()
            val list = mutableListOf<FavoritesItem>()

            for (f in favorites) {
                val conversation = messageRepository.getConversationFlow(user, f.username).firstOrNull() ?: emptyList()
                val last = conversation.lastOrNull()
                list.add(
                    FavoritesItem(
                        username = f.username,
                        lastMessageText = last?.messageText ?: "",
                        lastMessageStatus = last?.isRead ?: 0
                    )
                )
            }

            val filtered = filter(list, _uiState.value.searchText)
            _uiState.update { it.copy(favorites = list, filteredFavorites = filtered) }
        }
    }

    private fun filter(list: List<FavoritesItem>, query: String): List<FavoritesItem> {
        return if (query.isBlank()) list
        else list.filter { it.username.contains(query, ignoreCase = true) }
    }
}
