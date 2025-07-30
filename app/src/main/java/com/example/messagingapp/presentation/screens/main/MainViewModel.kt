package com.example.messagingapp.presentation.screens.main

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.messagingapp.model.data.UserEntity
import com.example.messagingapp.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private var currentUsername by mutableStateOf("")

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search

    private val _allContacts = MutableStateFlow<List<UserEntity>>(emptyList())
    val allContacts: StateFlow<List<UserEntity>> = _allContacts

    val filteredContacts: StateFlow<List<UserEntity>> =
        combine(_allContacts, _search) { contacts, query ->
            contacts.filter { it.username != currentUsername }
                .filter { it.username.contains(query, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    var showDeleteDialog by mutableStateOf(false)
        private set

    var contactToDelete by mutableStateOf<String?>(null)
        private set

    var selectedStatus by mutableStateOf("😎 Available")
        private set

    fun onSearchChanged(newSearch: String) {
        _search.value = newSearch
    }

    fun setLoggedInUser(username: String) {
        currentUsername = username
    }

    fun promptDeleteContact(contact: String) {
        contactToDelete = contact
        showDeleteDialog = true
    }

    fun confirmDeleteContact() {
        contactToDelete?.let {
            viewModelScope.launch {
                userRepository.deleteUser(it)
                _allContacts.value = userRepository.getAllUsers()
            }
        }
        contactToDelete = null
        showDeleteDialog = false
    }

    fun cancelDelete() {
        contactToDelete = null
        showDeleteDialog = false
    }

    fun refreshLocalUsers() {
        viewModelScope.launch {
            _allContacts.value = userRepository.getAllUsers()
        }
    }

    fun onStatusSelected(newStatus: String, username: String, context: Context) {
        viewModelScope.launch {
            userRepository.updateStatus(username, newStatus)
            selectedStatus = newStatus
            Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadOwnStatus(username: String) {
        viewModelScope.launch {
            selectedStatus = userRepository.getStatusForUser(username)
        }
    }

    fun toggleFavorite(username: String) {
        viewModelScope.launch {
            val user = _allContacts.value.find { it.username == username }
            if (user != null) {
                val newFavorite = !user.isFavorite
                userRepository.setFavorite(username, newFavorite)
                _allContacts.value = userRepository.getAllUsers()
            }
        }
    }
}
