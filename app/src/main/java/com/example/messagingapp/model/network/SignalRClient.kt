package com.example.messagingapp.model.network

import android.util.Log
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

    fun connect(baseUrl: String = "http://10.0.2.2:5263/SignalRHub") {
        Log.d("SignalR", "Connecting to SignalR...")
        if (hubConnection != null) return
        hubConnection = HubConnectionBuilder.create(baseUrl).build()

        hubConnection?.on("ReceiveMessage",
            { sender: String, receiver: String, text: String ->
                Log.d("SignalR", "Received message from $sender to $receiver: $text")
                CoroutineScope(Dispatchers.IO).launch {
                    _incomingMessages.emit(Triple(sender, receiver, text))
                }
            },
            String::class.java, String::class.java, String::class.java
        )

        hubConnection?.start()
            ?.doOnComplete {
                Log.d("SignalR", "SignalR connection started successfully.")
            }
            ?.doOnError {
                Log.e("SignalR", "SignalR connection failed!", it)
            }?.subscribe()
    }

    fun sendMessage(sender: String, receiver: String, message: String) {
        if (hubConnection == null) {
            Log.e("SignalR", "Cannot send message: HubConnection is null!")
            return
        }

        Log.d("SignalR", "Calling sendMessage to server: $sender → $receiver: $message")

        try {
            hubConnection?.send("SendMessage", sender, receiver, message)
            Log.d("SignalR", "sendMessage() called on hubConnection")
        } catch (e: Exception) {
            Log.e("SignalR", "Error sending message via SignalR", e)
        }
    }

    fun disconnect() {
        hubConnection?.stop()
        hubConnection = null
    }
}