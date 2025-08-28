package com.example.messagingapp.model.network

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("sender") val sender: String,
    @SerializedName("receiver") val receiver: String,
    @SerializedName("text") val text: String,
    @SerializedName("status") val status: Int? = null
)
