package com.example.messagingapp.presentation.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.repository.UserRepository
import com.example.messagingapp.utils.Hash
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UsernameChanged -> {
                uiState = uiState.copy(username = event.username)
            }

            is LoginUiEvent.PasswordChanged -> {
                uiState = uiState.copy(password = event.password)
            }

            is LoginUiEvent.LoginClicked -> {
                login()
            }
        }
    }

    private fun login() {
        if (uiState.username.isBlank() || uiState.password.isBlank()) {
            sendUiEvent(UiEvent.ShowToast("All fields are required"))
            return
        }

        viewModelScope.launch {
            val hashed = Hash.sha256(uiState.password)
            val result = userRepository.login(uiState.username, hashed)
            if (result) {
                userRepository.usersFromServer()
                uiState = uiState.copy(isLoginSuccessful = true)
                sendUiEvent(UiEvent.ShowToast("Login successful"))
            } else {
                sendUiEvent(UiEvent.ShowToast("Invalid credentials"))
            }
        }
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
