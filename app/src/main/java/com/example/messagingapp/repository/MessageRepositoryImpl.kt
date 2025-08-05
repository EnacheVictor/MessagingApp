package com.example.messagingapp.repository

import com.example.messagingapp.model.data.MessageDatabaseDao
import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.model.network.SignalRClient
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(private val dao: MessageDatabaseDao) : MessageRepository {

    override fun getConversationFlow(user1: String, user2: String): Flow<List<MessageEntity>> {
        return dao.getConversationFlow(user1, user2)
    }

    override suspend fun insertMessage(message: MessageEntity) {
        dao.insertMessage(message)
        SignalRClient.sendMessage(
            sender = message.senderUsername,
            receiver = message.receiverUsername,
            message = message.messageText
        )
    }

    override suspend fun deleteConversation(user1: String, user2: String) {
        dao.deleteConversation(user1, user2)
    }

    override suspend fun markMessagesAsRead(from: String, to: String) {
        dao.markMessagesAsRead(from, to)
    }
}

