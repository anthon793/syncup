package com.syncup.domain.model

import java.util.UUID

/**
 * Domain model for a project/workspace
 */
data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val owner: String, // userId
    val members: List<String> = emptyList(), // userIds
    val coverColor: String = "#6200EE",
    val coverImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val dueDate: Long? = null,
    val progress: Float = 0f,
    val isArchived: Boolean = false
)

/**
 * Domain model for project milestones (high-level phases)
 */
data class Milestone(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val description: String = "",
    val dueDate: Long,
    val progress: Float = 0f, // 0-1 (0-100%)
    val order: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val isCriticalPriority: Boolean = false,
    val associatedSubGroups: List<String> = emptyList()
)

/**
 * Domain model for individual tasks
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val milestoneId: String? = null,
    val title: String,
    val description: String? = null,
    val assignedTo: String? = null, // userId
    val dueDate: Long? = null,
    val status: TaskStatus = TaskStatus.TODO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val labels: List<String> = emptyList(),
    val attachments: List<String> = emptyList(), // fileIds
    val blockerReason: String? = null,
    val createdBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val isBacklog: Boolean = false
)

enum class TaskStatus {
    TODO, IN_PROGRESS, CRITICAL, DONE, BACKLOG
}

enum class TaskPriority {
    LOW, MEDIUM, HIGH, CRITICAL
}

/**
 * Domain model for deadlines with risk tracking
 */
data class DeadlineInfo(
    val taskId: String,
    val taskTitle: String,
    val dueDate: Long,
    val assignedUser: String,
    val riskLevel: RiskLevel = RiskLevel.NORMAL,
    val blockerActive: Boolean = false,
    val timeRemainingMs: Long = 0L
)

enum class RiskLevel {
    NORMAL, WARNING, CRITICAL
}
