package com.example.messagingapp.model.network

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
                if (dto.receiver == loggedInUser) {
                    val message = dto.toEntity(loggedInUser)
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