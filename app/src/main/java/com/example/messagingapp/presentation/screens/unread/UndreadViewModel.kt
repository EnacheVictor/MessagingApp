package com.example.messagingapp.presentation.screens.unread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.repository.MessageRepository
import com.example.messagingapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnreadViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    private val _unreadMessages = MutableStateFlow<List<UnreadItem>>(emptyList())
    val unreadMessages: StateFlow<List<UnreadItem>> = _unreadMessages

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search

    val filteredUnreadMessages: StateFlow<List<UnreadItem>> =
        combine(_unreadMessages, _search) { messages, query ->
            if (query.isBlank()) messages
            else messages.filter { it.username.contains(query, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var unreadJob: Job? = null

    fun onSearchChanged(newSearch: String) {
        _search.value = newSearch
    }

    fun loadUnreadMessages(loggedInUsername: String) {
        unreadJob?.cancel()
        unreadJob = viewModelScope.launch {
            val users = userRepository.getAllUsers()

            users.forEach { user ->
                launch {
                    messageRepository
                        .getConversationFlow(loggedInUsername, user.username)
                        .collect { conversation ->
                            val unread = conversation.filter {
                                it.receiverUsername == loggedInUsername && it.isRead < 2
                            }

                            if (unread.isNotEmpty()) {
                                val lastUnread = unread.last()
                                val item = UnreadItem(
                                    username = user.username,
                                    lastMessage = lastUnread.messageText,
                                    timestamp = lastUnread.timestamp
                                )
                                _unreadMessages.value =
                                    _unreadMessages.value.filterNot { it.username == item.username } + item
                            } else {
                                _unreadMessages.value =
                                    _unreadMessages.value.filterNot { it.username == user.username }
                            }
                        }
                }
            }
        }
    }

    fun onStatusSelected(newStatus: String, username: String) {
        viewModelScope.launch {
            userRepository.updateStatus(username, newStatus)
        }
    }

    data class UnreadItem(
        val username: String,
        val lastMessage: String,
        val timestamp: Long
    )
}