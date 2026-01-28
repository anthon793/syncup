package com.syncup.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for User
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val university: String,
    val profilePictureUrl: String?,
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Room entity for Project
 */
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val owner: String,
    val memberIds: String, // JSON string list
    val coverColor: String = "#6200EE",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)

/**
 * Room entity for Milestone
 */
@Entity(tableName = "milestones")
data class MilestoneEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val dueDate: Long,
    val progress: Float = 0f,
    val order: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)

/**
 * Room entity for Task
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val milestoneId: String?,
    val title: String,
    val description: String?,
    val assignedTo: String?,
    val dueDate: Long,
    val status: String = "TODO", // TaskStatus enum
    val priority: String = "MEDIUM", // TaskPriority enum
    val labels: String = "", // JSON string list
    val attachments: String = "", // JSON string list
    val blockerReason: String?,
    val createdBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Room entity for ActivityFeedItem
 */
@Entity(tableName = "activity_feed")
data class ActivityFeedEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val itemType: String, // TaskCompleted, FileUploaded, BlockerFlagged, etc.
    val data: String // JSON serialized activity item
)

/**
 * Room entity for SharedFile
 */
@Entity(tableName = "shared_files")
data class SharedFileEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String,
    val downloadUrl: String,
    val cloudProvider: String = "GOOGLE_DRIVE",
    val uploadedBy: String,
    val uploadedAt: Long = System.currentTimeMillis(),
    val versions: String = "[]", // JSON string list
    val lastModified: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)

/**
 * Room entity for ResourceLink
 */
@Entity(tableName = "resource_links")
data class ResourceLinkEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val title: String,
    val url: String,
    val icon: String?,
    val category: String = "BRIEF",
    val addedBy: String,
    val addedAt: Long = System.currentTimeMillis()
)

/**
 * Room entity for PresenceInfo
 */
@Entity(tableName = "presence_info")
data class PresenceInfoEntity(
    @PrimaryKey
    val userId: String,
    val userName: String,
    val isOnline: Boolean,
    val lastSeen: Long,
    val currentProjectId: String?
)
