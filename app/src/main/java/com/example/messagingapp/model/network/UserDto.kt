package com.example.messagingapp.model.network

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("username") val username: String,
    @SerializedName("publicKey") val publicKey: String
)
