package com.syncup.domain.model

/**
 * Chat message model for project and task-level discussions
 */
data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: ChatMessageType = ChatMessageType.TEXT,
    val attachmentUrl: String? = null
)

enum class ChatMessageType {
    TEXT, IMAGE, FILE, SYSTEM
}

/**
 * Chat conversation container (project-level or task-level)
 */
data class ChatConversation(
    val id: String,
    val conversationType: ConversationType,
    val targetId: String,  // projectId or taskId
    val messages: MutableList<ChatMessage> = mutableListOf(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class ConversationType {
    PROJECT_DISCUSSION,
    TASK_DISCUSSION
}
