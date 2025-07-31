package com.example.messagingapp.presentation.screens.signup

import com.example.messagingapp.model.data.PasswordRequirements

data class SignUpUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordRequirements: PasswordRequirements = PasswordRequirements(),
    val isSignUpSuccessful: Boolean = false
)
