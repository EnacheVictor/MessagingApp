package com.example.messagingapp.model.network

import com.google.gson.annotations.SerializedName


data class SignUpDto(
    @SerializedName("Username") val username: String,
    @SerializedName("Password") val password: String
)
