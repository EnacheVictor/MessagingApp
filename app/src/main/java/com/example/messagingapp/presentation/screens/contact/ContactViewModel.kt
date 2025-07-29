package com.example.messagingapp.presentation.screens.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: MessageRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages

    val filteredMessages: StateFlow<List<MessageEntity>> =
        combine(_messages, _searchText) { messages, search ->
            if (search.isBlank()) messages
            else messages.filter { it.messageText.contains(search, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onSearchChange(newText: String) {
        _searchText.value = newText
    }

    fun conversation(loggedInUser: String, contactUser: String) {
        viewModelScope.launch {
            repository.markMessagesAsRead(contactUser, loggedInUser)
            repository.getConversationFlow(loggedInUser, contactUser).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(sender: String, receiver: String, text: String) {
        viewModelScope.launch {
            val message = MessageEntity(
                senderUsername = sender,
                receiverUsername = receiver,
                messageText = text,
                timestamp = System.currentTimeMillis(),
                isRead = 0
            )
            repository.insertMessage(message)
        }
    }

    fun deleteConversation(user1: String, user2: String) {
        viewModelScope.launch {
            repository.deleteConversation(user1, user2)
        }
    }
}
