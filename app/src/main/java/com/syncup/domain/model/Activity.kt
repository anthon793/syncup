package com.syncup.domain.model

import java.util.UUID

/**
 * Domain model for activity feed items
 */
sealed class ActivityFeedItem {
    abstract val id: String
    abstract val projectId: String
    abstract val userId: String
    abstract val timestamp: Long

    data class TaskCompleted(
        override val id: String = UUID.randomUUID().toString(),
        override val projectId: String,
        override val userId: String,
        override val timestamp: Long = System.currentTimeMillis(),
        val taskId: String,
        val taskTitle: String,
        val userName: String
    ) : ActivityFeedItem()

    data class FileUploaded(
        override val id: String = UUID.randomUUID().toString(),
        override val projectId: String,
        override val userId: String,
        override val timestamp: Long = System.currentTimeMillis(),
        val fileId: String,
        val fileName: String,
        val fileSize: Long,
        val userName: String
    ) : ActivityFeedItem()

    data class BlockerFlagged(
        override val id: String = UUID.randomUUID().toString(),
        override val projectId: String,
        override val userId: String,
        override val timestamp: Long = System.currentTimeMillis(),
        val taskId: String,
        val taskTitle: String,
        val blockerReason: String,
        val userName: String
    ) : ActivityFeedItem()

    data class FriendlyNudge(
        override val id: String = UUID.randomUUID().toString(),
        override val projectId: String,
        override val userId: String,
        override val timestamp: Long = System.currentTimeMillis(),
        val targetUserId: String,
        val taskId: String,
        val taskTitle: String,
        val message: String,
        val userName: String
    ) : ActivityFeedItem()

    data class MilestoneCreated(
        override val id: String = UUID.randomUUID().toString(),
        override val projectId: String,
        override val userId: String,
        override val timestamp: Long = System.currentTimeMillis(),
        val milestoneId: String,
        val milestoneName: String,
        val dueDate: Long,
        val userName: String
    ) : ActivityFeedItem()

    data class Comment(
        override val id: String = UUID.randomUUID().toString(),
        override val projectId: String,
        override val userId: String,
        override val timestamp: Long = System.currentTimeMillis(),
        val itemId: String, // taskId or fileId
        val itemType: String, // "task" or "file"
        val content: String,
        val userName: String,
        val reactions: Map<String, Int> = emptyMap() // emoji -> count
    ) : ActivityFeedItem()
}

/**
 * Domain model for collaboration feed in project context
 */
data class CollaborationEvent(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val activityItem: ActivityFeedItem,
    val isRead: Boolean = false,
    val reactions: Map<String, List<String>> = emptyMap() // emoji -> userIds
)

/**
 * Domain model for real-time presence data
 */
data class PresenceInfo(
    val userId: String,
    val userName: String,
    val isOnline: Boolean,
    val lastSeen: Long,
    val currentProjectId: String? = null
)
