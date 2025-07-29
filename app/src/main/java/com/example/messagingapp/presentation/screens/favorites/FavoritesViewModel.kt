package com.example.messagingapp.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.repository.MessageRepository
import com.example.messagingapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _favoriteItems = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favoriteItems: StateFlow<List<FavoriteItem>> = _favoriteItems

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search

    val filteredFavorites: StateFlow<List<FavoriteItem>> = combine(
        _favoriteItems, _search
    ) { items, query ->
        if (query.isBlank()) items
        else items.filter { it.username.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var favoritesJob: Job? = null

    fun onSearchChanged(newSearch: String) {
        _search.value = newSearch
    }

    fun loadFavoriteUsers(loggedInUsername: String) {
        favoritesJob?.cancel()
        favoritesJob = viewModelScope.launch {
            val favorites = userRepository.getFavorites()
            favorites.forEach { favoriteUser ->
                launch {
                    messageRepository.getConversationFlow(loggedInUsername, favoriteUser.username)
                        .collect { messages ->
                            val last = messages.lastOrNull()
                            val item = FavoriteItem(
                                username = favoriteUser.username,
                                lastMessageText = last?.messageText ?: "",
                                lastMessageStatus = last?.isRead ?: 0
                            )
                            _favoriteItems.value =
                                _favoriteItems.value.filterNot { it.username == item.username } + item
                        }
                }
            }
        }
    }

    fun removeFavorite(username: String) {
        viewModelScope.launch {
            userRepository.removeFromFavorites(username)
            _favoriteItems.value = _favoriteItems.value.filterNot { it.username == username }
        }
    }

    fun onStatusSelected(newStatus: String, username: String) {
        viewModelScope.launch {
            userRepository.updateStatus(username, newStatus)
        }
    }
}
data class FavoriteItem(
    val username: String,
    val lastMessageStatus: Int,
    val lastMessageText: String
)