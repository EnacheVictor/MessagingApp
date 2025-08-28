package com.example.messagingapp.model.network

import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.model.data.UserEntity

fun MessageDto.toEntity(myUser: String) = MessageEntity(
    senderUsername = sender,
    receiverUsername = receiver,
    messageText = text,
    isRead = 1,
    timestamp = System.currentTimeMillis()
)

fun UserDto.toEntity() = UserEntity(
    username = username,
    publicKey = publicKey
)