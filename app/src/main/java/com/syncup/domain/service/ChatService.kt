package com.syncup.domain.service

import com.syncup.domain.model.*

/**
 * Chat service for managing project and task-level discussions
 */
class ChatService {
    
    private val conversations = mutableMapOf<String, ChatConversation>()
    
    /**
     * Send a message to project or task conversation
     */
    fun sendMessage(
        conversationType: ConversationType,
        targetId: String,
        sender: RoleBasedUser,
        content: String
    ): Result<ChatMessage> {
        return if (sender.canSendMessages()) {
            val conversationId = "${conversationType.name}_$targetId"
            val conversation = conversations.getOrPut(conversationId) {
                ChatConversation(
                    id = conversationId,
                    conversationType = conversationType,
                    targetId = targetId
                )
            }
            
            val message = ChatMessage(
                id = "msg_${System.currentTimeMillis()}",
                senderId = sender.id,
                senderName = sender.name,
                content = content,
                timestamp = System.currentTimeMillis()
            )
            
            conversation.messages.add(message)
            Result.success(message)
        } else {
            Result.failure(Exception("User doesn't have permission to send messages"))
        }
    }
    
    /**
     * Get conversation history
     */
    fun getConversationHistory(
        conversationType: ConversationType,
        targetId: String
    ): List<ChatMessage> {
        val conversationId = "${conversationType.name}_$targetId"
        return conversations[conversationId]?.messages?.sortedBy { it.timestamp } ?: emptyList()
    }
    
    /**
     * Get all conversations for a target
     */
    fun getConversation(
        conversationType: ConversationType,
        targetId: String
    ): ChatConversation? {
        val conversationId = "${conversationType.name}_$targetId"
        return conversations[conversationId]
    }
    
    /**
     * Get unread message count
     */
    fun getUnreadMessageCount(
        conversationType: ConversationType,
        targetId: String,
        userId: String
    ): Int {
        val history = getConversationHistory(conversationType, targetId)
        return history.count { it.senderId != userId }
    }
}
