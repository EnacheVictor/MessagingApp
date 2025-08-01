package com.example.messagingapp.presentation.screens.login

sealed class LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    object LoginClicked : LoginUiEvent()
}