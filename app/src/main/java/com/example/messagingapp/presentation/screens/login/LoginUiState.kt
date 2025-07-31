package com.example.messagingapp.presentation.screens.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginSuccessful: Boolean = false
)

