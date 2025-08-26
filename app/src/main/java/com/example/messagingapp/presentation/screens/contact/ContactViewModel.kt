package com.example.messagingapp.presentation.screens.contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: MessageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    fun onEvent(event: ContactUiEvent) {
        when (event) {
            is ContactUiEvent.SearchChanged -> {
                val filtered = _uiState.value.messages.filter {
                    it.messageText.contains(event.newText, ignoreCase = true)
                }
                _uiState.update {
                    it.copy(
                        searchText = event.newText,
                        filteredMessages = filtered
                    )
                }
            }

            is ContactUiEvent.SendMessage -> {
                viewModelScope.launch {
                    Log.d("Crypto", "📝 User is sending message: ${event.text} from ${event.sender} to ${event.receiver}")

                    val message = MessageEntity(
                        senderUsername = event.sender,
                        receiverUsername = event.receiver,
                        messageText = event.text,
                        timestamp = System.currentTimeMillis(),
                        isRead = 0
                    )
                    repository.insertMessage(message)
                }
            }

            is ContactUiEvent.DeleteConversation -> {
                viewModelScope.launch {
                    repository.deleteConversation(event.user1, event.user2)
                }
            }

            is ContactUiEvent.LoadConversation -> {
                viewModelScope.launch {
                    repository.markMessagesAsRead(event.contactUser, event.loggedInUser)
                    repository.getConversationFlow(event.loggedInUser, event.contactUser).collect { list ->
                        Log.d("Crypto", "Collected message list from Flow. Size: ${list.size}")
                        _uiState.update {
                            it.copy(
                                messages = list,
                                filteredMessages = applyFilter(list, _uiState.value.searchText)
                            )
                        }
                    }
                }
            }
        }
    }
    private fun applyFilter(messages: List<MessageEntity>, search: String): List<MessageEntity> {
        return if (search.isBlank()) messages
        else messages.filter { it.messageText.contains(search, ignoreCase = true) }
    }
}


