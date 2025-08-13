package com.example.messagingapp.model.network

import android.util.Log
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SignalRClient {

    private var hubConnection: HubConnection? = null

    var isConnected = false
        private set

    private val _incomingMessages = MutableSharedFlow<Triple<String, String, String>>()
    val incomingMessages = _incomingMessages.asSharedFlow()

    fun connect(username: String, baseUrl: String = "http://10.0.2.2:5263/SignalRHub") {
        if (hubConnection != null) return
        val url = "$baseUrl?user=$username"
        Log.d("SignalR", "Connecting to SignalR as $username ...")

        hubConnection = HubConnectionBuilder.create(url).build()

        hubConnection?.on("ReceiveMessage",
            { sender: String, receiver: String, text: String ->
                CoroutineScope(Dispatchers.IO).launch {
                    _incomingMessages.emit(Triple(sender, receiver, text))
                }
            },
            String::class.java, String::class.java, String::class.java
        )

        hubConnection?.start()
            ?.doOnComplete {
                isConnected = true
                Log.d("SignalR", "SignalR connection started.")
            }
            ?.doOnError {
                isConnected = false
                Log.e("SignalR", "SignalR connection failed", it)
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

    suspend fun getMessagesWhenDisconnected(loggedInUser: String): List<MessageDto> =
        withContext(Dispatchers.IO) {
            hubConnection
                ?.invoke(Array<MessageDto>::class.java, "GetAndDeleteMissedMessages", loggedInUser)
                ?.blockingGet()
                ?.toList()
                ?: emptyList()
        }

    fun disconnect() {
        hubConnection?.stop()
        hubConnection = null
        isConnected = false
    }
}