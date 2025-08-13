package com.example.messagingapp.repository

import android.util.Log
import com.example.messagingapp.model.data.MessageDatabaseDao
import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.model.network.SignalRClient
import com.example.messagingapp.model.network.toEntity
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(private val dao: MessageDatabaseDao) : MessageRepository {

    override fun getConversationFlow(user1: String, user2: String): Flow<List<MessageEntity>> {
        return dao.getConversationFlow(user1, user2)
    }

    override suspend fun insertMessage(message: MessageEntity, skipSignalR: Boolean) {
        Log.d("MessageRepo", "Saving message local: ${message.messageText}")
        dao.insertMessage(message)

        if (!skipSignalR) {
            Log.d("MessageRepo", "Sending message via SignalR: ${message.senderUsername} → ${message.receiverUsername}")
            SignalRClient.sendMessage(
                sender = message.senderUsername,
                receiver = message.receiverUsername,
                message = message.messageText
            )
        } else {
            Log.d("MessageRepo", "Skipped SignalR send (because message is from SignalR)")
        }
    }

    override suspend fun deleteConversation(user1: String, user2: String) {
        dao.deleteConversation(user1, user2)
    }

    override suspend fun markMessagesAsRead(from: String, to: String) {
        dao.markMessagesAsRead(from, to)
    }

    override suspend fun getMissedMessages(loggedInUser: String) {
        val missed = SignalRClient.getMessagesWhenDisconnected(loggedInUser)
        Log.d(
            "MessageRepo",
            "📦 Mesajele au fost primite de la server: ${missed.size} item(s) pentru $loggedInUser"
        )
        if (missed.isEmpty()) return
        missed.forEach { dto ->
            insertMessage(dto.toEntity(loggedInUser), skipSignalR = true)
        }
    }
}


