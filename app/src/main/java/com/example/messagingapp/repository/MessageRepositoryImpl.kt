package com.example.messagingapp.repository

import android.util.Log
import com.example.messagingapp.domain.crypto.CryptoRepository
import com.example.messagingapp.domain.crypto.EncryptorAndDecryptor
import com.example.messagingapp.model.data.MessageDatabaseDao
import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.model.network.SignalRClient
import com.example.messagingapp.model.network.toEntity
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(private val dao: MessageDatabaseDao,
                            private val userRepo: UserRepository,
                            private val keysRepo: KeysRepository,
                            private val crypto: CryptoRepository) : MessageRepository {

    private val encryptor = EncryptorAndDecryptor(userRepo, keysRepo, crypto)

    override fun getConversationFlow(user1: String, user2: String): Flow<List<MessageEntity>> {
        return dao.getConversationFlow(user1, user2)
    }

    override suspend fun insertMessage(message: MessageEntity, skipSignalR: Boolean) {
        try {
            if (skipSignalR) {
                val plain = try {
                    encryptor.decryptFrom(message.senderUsername, message.messageText)
                } catch (e: Throwable) {
                    Log.e("Crypto", "Decrypt exception: ${e.message}", e)
                    null
                }

                val toSave = if (plain.isNullOrBlank()) {
                    Log.w("Crypto", "Decrypt failed/empty. Saving raw text as fallback.")
                    message.copy(isRead = 1)
                } else {
                    message.copy(messageText = plain, isRead = 1)
                }

                dao.insertMessage(toSave)
                Log.d("Crypto", "Incoming saved in clear: ${toSave.senderUsername} → ${toSave.receiverUsername}")
            } else {
                dao.insertMessage(message)
                Log.d("Crypto", "Outgoing saved in clear: ${message.senderUsername} → ${message.receiverUsername}")

                val ctB64 = try {
                    encryptor.encryptFor(message.receiverUsername, message.messageText)
                } catch (e: Throwable) {
                    Log.e("Crypto", "Encrypt exception: ${e.message}", e)
                    null
                }

                if (ctB64.isNullOrBlank()) {
                    Log.w("Crypto", "Encryption failed; sending plaintext as fallback.")
                    SignalRClient.sendMessage(
                        sender = message.senderUsername,
                        receiver = message.receiverUsername,
                        message = message.messageText
                    )
                } else {
                    Log.d("Crypto", "Sending ENCRYPTED via SignalR")
                    SignalRClient.sendMessage(
                        sender = message.senderUsername,
                        receiver = message.receiverUsername,
                        message = ctB64
                    )
                }
            }
        } catch (t: Throwable) {
            Log.e("Crypto", "insertMessage failed: ${t.message}", t)
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
            "Crypto",
            "Mesajele au fost primite de la server: ${missed.size} item(s) pentru $loggedInUser"
        )
        if (missed.isEmpty()) return
        missed.forEach { dto ->
            insertMessage(dto.toEntity(loggedInUser), skipSignalR = true)
        }
    }
}


