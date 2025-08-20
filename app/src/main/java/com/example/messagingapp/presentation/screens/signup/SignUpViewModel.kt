package com.example.messagingapp.presentation.screens.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messagingapp.domain.crypto.KeyGen
import com.example.messagingapp.domain.crypto.usecases.ExportPublicKeyAsStringUseCase
import com.example.messagingapp.model.data.PasswordRequirements
import com.example.messagingapp.repository.UserRepository
import com.example.messagingapp.utils.Hash
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val exportPublicKeyAsString: ExportPublicKeyAsStringUseCase
) : ViewModel() {

    var uiState by mutableStateOf(SignUpUiState())
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.UsernameChanged -> uiState = uiState.copy(username = event.username)
            is SignUpUiEvent.EmailChanged -> uiState = uiState.copy(email = event.email)
            is SignUpUiEvent.PasswordChanged -> {
                uiState = uiState.copy(
                    password = event.password,
                    passwordRequirements = checkPasswordRequirements(event.password)
                )
            }
            is SignUpUiEvent.ConfirmPasswordChanged -> uiState = uiState.copy(confirmPassword = event.confirmPassword)
            is SignUpUiEvent.SignUpClicked -> signUp()
        }
    }

    private fun signUp() {
        val error = validateInputs()
        if (error != null) {
            sendUiEvent(UiEvent.ShowToast(error))
            return
        }

        viewModelScope.launch {
            val keyPair = KeyGen.generateKeyPair()
            val publicKeyString = exportPublicKeyAsString(keyPair.public)
            val hashedPassword = Hash.sha256(uiState.password)

            Log.d("Crypto", "PublicKey(Base64) to be sent: $publicKeyString")
            Log.d("SignUp", "Username=${uiState.username}, PasswordHash=$hashedPassword")

            val result = userRepository.signUp(uiState.username, hashedPassword, publicKeyString)
            if (result) {
                sendUiEvent(UiEvent.ShowToast("Sign Up Successful"))
                uiState = uiState.copy(isSignUpSuccessful = true)
            } else {
                sendUiEvent(UiEvent.ShowToast("Internal error"))
            }
        }
    }

    private fun validateInputs(): String? {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return when {
            uiState.username.isBlank() || uiState.email.isBlank() || uiState.password.isBlank() || uiState.confirmPassword.isBlank() ->
                "All fields are required."
            !uiState.email.matches(emailRegex) -> "Invalid email format."
            uiState.password != uiState.confirmPassword -> "Passwords do not match."
            else -> null
        }
    }

    private fun checkPasswordRequirements(password: String): PasswordRequirements {
        return PasswordRequirements(
            lengthOk = password.length >= 8,
            hasUppercase = password.any { it.isUpperCase() },
            hasLowercase = password.any { it.isLowerCase() },
            hasDigit = password.any { it.isDigit() },
            hasSpecialChar = password.any { !it.isLetterOrDigit() }
        )
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
