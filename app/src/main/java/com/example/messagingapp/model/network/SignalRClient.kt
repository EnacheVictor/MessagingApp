package com.example.messagingapp.model.network

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object SignalRClient {

    private var hubConnection: HubConnection? = null

    private val _incomingMessages = MutableSharedFlow<Triple<String, String, String>>()
    val incomingMessages = _incomingMessages.asSharedFlow()

    fun connect(baseUrl: String = "http://10.0.2.2:5263/messagehub") {
        if (hubConnection != null) return
        hubConnection = HubConnectionBuilder.create(baseUrl).build()

        hubConnection?.on("ReceiveMessage",
            { sender: String, receiver: String, text: String ->
                CoroutineScope(Dispatchers.IO).launch {
                    _incomingMessages.emit(Triple(sender, receiver, text))
                }
            },
            String::class.java, String::class.java, String::class.java
        )

        hubConnection?.start()
    }

    fun sendMessage(sender: String, receiver: String, message: String) {
        hubConnection?.send("SendMessage", sender, receiver, message)
    }

    fun disconnect() {
        hubConnection?.stop()
        hubConnection = null
    }
}