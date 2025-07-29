package com.example.messagingapp.presentation.screens.signup

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.messagingapp.model.data.PasswordRequirements

class SignUpViewModel: ViewModel() {

    var username by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var passwordRequirementsState by mutableStateOf(
        PasswordRequirements()
    )
        private set

    var isSignUpSuccessful by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername:String){
        username = newUsername
    }

    fun onEmailChanged(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        passwordRequirementsState = checkPasswordRequirements(newPassword)
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
    }

    private fun validateInputs(): String? {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        return when {
            username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                "All fields are required."
            !email.matches(emailRegex) ->
                "Invalid email format."
            password != confirmPassword ->
                "Passwords do not match."
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


    fun onSignUpClicked(context: Context){

        errorMessage = validateInputs()
        if (errorMessage == null) {
            Toast.makeText(
                context,
                "Sign Up Successful",
                Toast.LENGTH_LONG
            ).show()
            isSignUpSuccessful = true
        } else {
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }

    }

}