package com.example.messagingapp.presentation.screens.unread

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.repository.MessageRepository
import com.example.messagingapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import com.example.messagingapp.presentation.components.UnreadItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnreadViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(UnreadUiState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var unreadJob: Job? = null

    fun onEvent(event: UnreadUiEvent) {
        when (event) {
            is UnreadUiEvent.Init -> {
                uiState = uiState.copy(loggedInUsername = event.loggedInUsername)
                loadUnreadMessages()
            }

            is UnreadUiEvent.SearchChanged -> {
                val filtered = filter(uiState.unreadItems, event.text)
                uiState = uiState.copy(searchText = event.text, filteredUnreadItems = filtered)
            }

            is UnreadUiEvent.StatusSelected -> {
                viewModelScope.launch {
                    userRepository.updateStatus(uiState.loggedInUsername, event.status)
                    sendUiEvent(UiEvent.ShowToast("Status updated"))
                }
            }
        }
    }

    private fun loadUnreadMessages() {
        unreadJob?.cancel()
        unreadJob = viewModelScope.launch {
            val users = userRepository.getAllUsers()

            users.forEach { user ->
                launch {
                    messageRepository
                        .getConversationFlow(uiState.loggedInUsername, user.username)
                        .collect { conversation ->
                            val unread = conversation.filter {
                                it.receiverUsername == uiState.loggedInUsername && it.isRead < 3
                            }

                            val updatedList = if (unread.isNotEmpty()) {
                                val last = unread.last()
                                val item = UnreadItem(
                                    username = user.username,
                                    lastMessage = last.messageText,
                                    timestamp = last.timestamp
                                )
                                uiState.unreadItems.filterNot { it.username == user.username } + item
                            } else {
                                uiState.unreadItems.filterNot { it.username == user.username }
                            }

                            val filtered = filter(updatedList, uiState.searchText)
                            uiState = uiState.copy(
                                unreadItems = updatedList,
                                filteredUnreadItems = filtered
                            )
                        }
                }
            }
        }
    }

    private fun filter(list: List<UnreadItem>, query: String): List<UnreadItem> {
        return if (query.isBlank()) list
        else list.filter { it.username.contains(query, ignoreCase = true) }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}
