package com.example.messagingapp.presentation.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

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

    fun onLoginClicked(context: Context) {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "All fields are required"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            return
        }

        viewModelScope.launch {
            val result = userRepository.login(username, password)
            if (result) {
                userRepository.usersFromServer()
                Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()
                isLoginSuccessful = true
            } else {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
            }
        }
    }
}
