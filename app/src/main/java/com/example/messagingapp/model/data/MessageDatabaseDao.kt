package com.example.messagingapp.model.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDatabaseDao {

    @Query("SELECT * FROM messages WHERE (senderUsername = :user1 AND receiverUsername = :user2) OR (senderUsername = :user2 AND receiverUsername = :user1) ORDER BY timestamp ASC")
    fun getConversationFlow(user1: String, user2: String): Flow<List<MessageEntity>>

    @Upsert
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages WHERE (senderUsername = :user1 AND receiverUsername = :user2) OR (senderUsername = :user2 AND receiverUsername = :user1)")
    suspend fun deleteConversation(user1: String, user2: String)

    @Query("UPDATE messages SET isRead = 3 WHERE senderUsername = :from AND receiverUsername = :to")
    suspend fun markMessagesAsRead(from: String, to: String)
}
