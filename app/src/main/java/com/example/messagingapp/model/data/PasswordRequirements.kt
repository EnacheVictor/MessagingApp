package com.example.messagingapp.model.data

data class PasswordRequirements(
    val lengthOk: Boolean = false,
    val hasUppercase: Boolean = false,
    val hasLowercase: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialChar: Boolean = false
)
