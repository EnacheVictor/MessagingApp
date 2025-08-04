package com.example.messagingapp.model.network

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
                SignalRClient.incomingMessages.collectLatest { (sender, receiver, text) ->
                    val isForMe = receiver == loggedInUser
                    if (isForMe) {
                        val message = MessageEntity(
                            senderUsername = sender,
                            receiverUsername = receiver,
                            messageText = text,
                            timestamp = System.currentTimeMillis(),
                            isRead = 1
                        )
                        messageRepository.insertMessage(message)
                    }
                }
            }
        }

        fun stopListening() {
            scope?.cancel()
            scope = null
        }
    }