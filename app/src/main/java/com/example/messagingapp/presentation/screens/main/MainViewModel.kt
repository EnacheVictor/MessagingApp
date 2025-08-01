package com.example.messagingapp.presentation.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.model.data.UserEntity
import com.example.messagingapp.repository.MessageRepository
import com.example.messagingapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    var uiState by mutableStateOf(MainUiState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.Init -> {
                uiState = uiState.copy(currentUsername = event.loggedInUsername)
                loadOwnStatus()
                refreshContacts()
            }

            is MainUiEvent.SearchChanged -> {
                val filtered = filterContacts(uiState.allContacts, event.text)
                uiState = uiState.copy(searchText = event.text, filteredContacts = filtered)
            }

            is MainUiEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    userRepository.setFavorite(event.username, !getUser(event.username)?.isFavorite!!)
                    refreshContacts()
                }
            }

            is MainUiEvent.PromptDelete -> {
                uiState = uiState.copy(contactToDelete = event.username, showDeleteDialog = true)
            }

            is MainUiEvent.ConfirmDelete -> {
                viewModelScope.launch {
                    uiState.contactToDelete?.let { userRepository.deleteUser(it) }
                    refreshContacts()
                }
                uiState = uiState.copy(contactToDelete = null, showDeleteDialog = false)
            }

            is MainUiEvent.CancelDelete -> {
                uiState = uiState.copy(contactToDelete = null, showDeleteDialog = false)
            }

            is MainUiEvent.StatusSelected -> {
                viewModelScope.launch {
                    userRepository.updateStatus(uiState.currentUsername, event.status)
                    uiState = uiState.copy(selectedStatus = event.status)
                    sendUiEvent(UiEvent.ShowToast("Status updated"))
                }
            }
        }
    }

    private fun refreshContacts() {
        viewModelScope.launch {
            val all = userRepository.getAllUsers()
            val filtered = filterContacts(all, uiState.searchText)
            uiState = uiState.copy(allContacts = all, filteredContacts = filtered)
        }
    }

    private fun loadOwnStatus() {
        viewModelScope.launch {
            val status = userRepository.getStatusForUser(uiState.currentUsername)
            uiState = uiState.copy(selectedStatus = status)
        }
    }

    private fun getUser(username: String): UserEntity? {
        return uiState.allContacts.find { it.username == username }
    }

    private fun filterContacts(contacts: List<UserEntity>, query: String): List<UserEntity> {
        return contacts.filter { it.username != uiState.currentUsername }
            .filter { it.username.contains(query, ignoreCase = true) }
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
