package com.example.messagingapp.model.network

import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.model.data.UserEntity

fun MessageDto.toEntity(myUser: String) = MessageEntity(
    senderUsername = sender,
    receiverUsername = receiver,
    messageText = text,
    isRead = if (receiver == myUser) 1 else 0,
    timestamp = System.currentTimeMillis()
)

fun UserDto.toEntity() = UserEntity(
    username = username,
    publicKey = publicKey
)