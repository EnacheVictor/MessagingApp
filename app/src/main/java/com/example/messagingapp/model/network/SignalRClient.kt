package com.example.messagingapp.model.network

import android.util.Log
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SignalRClient {

    private var hubConnection: HubConnection? = null
    private var connectedUser: String? = null

    @Volatile
    var isConnected = false
        private set

    private val _incomingMessages = MutableSharedFlow<MessageDto>(extraBufferCapacity = 32)
    val incomingMessages = _incomingMessages.asSharedFlow()

    @Synchronized
    fun connect(username: String, baseUrl: String = "http://10.0.2.2:5263") {
        if (hubConnection != null &&
            connectedUser == username &&
            hubConnection?.connectionState == HubConnectionState.CONNECTED) {
            Log.d("Crypto", "Already connected as $username")
            return
        }

        if (hubConnection != null && connectedUser != username) {
            Log.d("Crypto", "Switching user ${connectedUser} -> $username, reconnecting…")
            disconnect()
        }

        val url = "${baseUrl.trimEnd('/')}/chatHub?user=$username"
        Log.d("Crypto", "Connecting to SignalR as $username @ $url")

        hubConnection = HubConnectionBuilder.create(url).build()
        connectedUser = username

        hubConnection?.on(
            "ReceiveMessage",
            { dto: MessageDto ->
                CoroutineScope(Dispatchers.IO).launch { _incomingMessages.emit(dto) }
            },
            MessageDto::class.java
        )

        hubConnection?.onClosed {
            isConnected = false
            Log.w("Crypto", "Connection closed", it)
        }

        hubConnection?.start()
            ?.doOnComplete {
                isConnected = true
                Log.d("Crypto", "SignalR connection started as $username.")
            }
            ?.doOnError {
                isConnected = false
                Log.e("Crypto", "SignalR connection failed", it)
            }
            ?.subscribe()
    }

    fun sendMessage(sender: String, receiver: String, message: String) {
        val hc = hubConnection
        if (hc == null) {
            Log.e("Crypto", "Cannot send: HubConnection is null!")
            return
        }
        try {
            hc.send("SendMessage", MessageDto(sender = sender, receiver = receiver, text = message))
        } catch (e: Exception) {
            Log.e("Crypto", "Error sending message via SignalR", e)
        }
    }

    private suspend fun waitUntilConnected(timeoutMs: Long = 5000) {
        val start = System.currentTimeMillis()
        while (hubConnection?.connectionState != HubConnectionState.CONNECTED) {
            if (System.currentTimeMillis() - start > timeoutMs) break
            delay(50)
        }
    }

    suspend fun getMessagesWhenDisconnected(loggedInUser: String): List<MessageDto> =
        withContext(Dispatchers.IO) {
            waitUntilConnected()
            try {
                hubConnection
                    ?.invoke(Array<MessageDto>::class.java, "GetMissedMessages", loggedInUser)
                    ?.blockingGet()
                    ?.toList()
                    ?: emptyList()
            } catch (ex: Throwable) {
                hubConnection
                    ?.invoke(Array<MessageDto>::class.java, "GetAndDeleteMissedMessages", loggedInUser)
                    ?.blockingGet()
                    ?.toList()
                    ?: emptyList()
            }
        }
    fun disconnect() {
        try {
            hubConnection?.stop()?.blockingAwait()
        } catch (e: Exception) {
            Log.w("Crypto", "Stop failed", e)
        } finally {
            hubConnection = null
            connectedUser = null
            isConnected = false
        }
    }
}
