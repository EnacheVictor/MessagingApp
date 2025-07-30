package com.example.messagingapp.model.network

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("Username") val username: String,
    @SerializedName("Password") val password: String
)
