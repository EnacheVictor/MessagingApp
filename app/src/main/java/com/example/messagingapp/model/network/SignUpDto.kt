package com.example.messagingapp.model.network

import com.google.gson.annotations.SerializedName


data class SignUpDto(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("publicKey") val publicKey: String

)
