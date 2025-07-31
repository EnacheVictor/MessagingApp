package com.example.messagingapp.presentation.screens.signup

sealed class SignUpUiEvent {
    data class UsernameChanged(val username: String) : SignUpUiEvent()
    data class EmailChanged(val email: String) : SignUpUiEvent()
    data class PasswordChanged(val password: String) : SignUpUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpUiEvent()
    object SignUpClicked : SignUpUiEvent()
}