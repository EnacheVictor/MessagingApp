package com.example.messagingapp.repository

import com.example.messagingapp.model.data.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getConversationFlow(user1: String, user2: String): Flow<List<MessageEntity>>
    suspend fun insertMessage(message: MessageEntity)
    suspend fun deleteConversation(user1: String, user2: String)
    suspend fun markMessagesAsRead(from: String, to: String)
}