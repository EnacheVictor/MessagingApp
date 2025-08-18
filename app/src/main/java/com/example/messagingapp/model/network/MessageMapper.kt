package com.example.messagingapp.model.network

import com.example.messagingapp.model.data.MessageEntity

fun MessageDto.toEntity(myUser: String) = MessageEntity(
    senderUsername = sender,
    receiverUsername = receiver,
    messageText = text,
    isRead = if (receiver == myUser) 1 else 0,
    timestamp = System.currentTimeMillis()
)