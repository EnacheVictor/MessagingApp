package com.example.messagingapp.model.network

import android.util.Log
import com.example.messagingapp.model.data.MessageEntity
import com.example.messagingapp.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object SignalRListener {

        private var scope: CoroutineScope? = null

    fun startListening(loggedInUser: String, messageRepository: MessageRepository) {
        if (scope != null) return

        scope = CoroutineScope(Dispatchers.IO)

        scope?.launch {
            SignalRClient.incomingMessages.collectLatest { dto ->
                val isForMe = dto.receiver == loggedInUser
                if (isForMe) {
                    Log.d("SignalRListener", "Message is for logged-in user: $loggedInUser")

                    val message = MessageEntity(
                        senderUsername = dto.sender,
                        receiverUsername = dto.receiver,
                        messageText = dto.text,
                        timestamp = System.currentTimeMillis(),
                        isRead = 1
                    )

                    messageRepository.insertMessage(message, skipSignalR = true)
                }
            }
        }
    }

        fun stopListening() {
            scope?.cancel()
            scope = null
        }
    }