package com.example.messagingapp.model.network

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("Username") val username: String,
    @SerializedName("PublicKey") val publicKey: String
)
