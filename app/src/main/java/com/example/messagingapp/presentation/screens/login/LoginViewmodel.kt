package com.example.messagingapp.presentation.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoginSuccessful by mutableStateOf(false)
        private set


    fun onPasswordChanged(newPassword: String) {
        password = newPassword
    }

    fun onUsernameChanged(newUsername: String) {
        username = newUsername
    }

    private fun validateInputs(): String? {

        return when {
            username.isBlank() || password.isBlank() -> "All fields are required."
            else -> null
        }
    }

    fun onLoginClicked(context: Context) {
        errorMessage = validateInputs()
        if (errorMessage == null) {
            isLoginSuccessful = true
            Toast.makeText(
                context,
                "Login successful",
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
