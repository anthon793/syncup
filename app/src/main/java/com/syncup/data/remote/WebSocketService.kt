package com.syncup.data.remote

import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocket

interface WebSocketService {
    fun connect(url: String): Flow<WebSocketMessage>
    fun disconnect()
    suspend fun send(message: String)
    fun isConnected(): Boolean
}

data class WebSocketMessage(
    val type: MessageType,
    val data: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageType {
    ACTIVITY_UPDATE,
    PRESENCE_UPDATE,
    TASK_STATUS_CHANGE,
    BLOCKER_FLAGGED,
    BLOCKER_RESOLVED,
    FILE_UPLOADED,
    NUDGE_SENT,
    COMMENT_ADDED,
    ERROR,
    PING,
    PONG
}

class WebSocketServiceImpl(
    private val okHttpClient: okhttp3.OkHttpClient
) : WebSocketService {
    private var webSocket: WebSocket? = null
    private val messageFlow = kotlinx.coroutines.flow.MutableSharedFlow<WebSocketMessage>()

    override fun connect(url: String): Flow<WebSocketMessage> {
        val request = okhttp3.Request.Builder().url(url).build()
        val listener = SyncUpWebSocketListener { message ->
            // Emit messages to flow
        }
        webSocket = okHttpClient.newWebSocket(request, listener)
        return messageFlow
    }

    override fun disconnect() {
        webSocket?.close(1000, "Normal closure")
        webSocket = null
    }

    override suspend fun send(message: String) {
        webSocket?.send(message)
    }

    override fun isConnected(): Boolean {
        return webSocket != null
    }
}

class SyncUpWebSocketListener(
    private val onMessageReceived: (WebSocketMessage) -> Unit
) : okhttp3.WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        // Parse incoming message and emit it
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(1000, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        super.onFailure(webSocket, t, response)
    }
}
