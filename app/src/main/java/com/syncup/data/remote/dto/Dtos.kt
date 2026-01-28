package com.syncup.data.remote.dto

/**
 * DTOs for API communication
 */

// User DTOs
data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val university: String,
    val profilePictureUrl: String?,
    val isOnline: Boolean,
    val lastSeen: Long,
    val createdAt: Long
)

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val university: String
)

data class SignUpResponse(
    val success: Boolean,
    val message: String,
    val user: UserDto?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val user: UserDto?
)

data class AuthTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

// Project DTOs
data class ProjectDto(
    val id: String,
    val name: String,
    val description: String,
    val owner: String,
    val memberIds: List<String>,
    val coverColor: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isArchived: Boolean
)

data class CreateProjectRequest(
    val name: String,
    val description: String,
    val coverColor: String = "#6200EE"
)

// Task DTOs
data class TaskDto(
    val id: String,
    val projectId: String,
    val milestoneId: String?,
    val title: String,
    val description: String?,
    val assignedTo: String?,
    val dueDate: Long,
    val status: String,
    val priority: String,
    val labels: List<String>,
    val attachments: List<String>,
    val blockerReason: String?,
    val createdBy: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class CreateTaskRequest(
    val projectId: String,
    val milestoneId: String?,
    val title: String,
    val description: String?,
    val assignedTo: String?,
    val dueDate: Long,
    val priority: String = "MEDIUM"
)

data class UpdateTaskStatusRequest(
    val taskId: String,
    val status: String
)

data class FlagBlockerRequest(
    val taskId: String,
    val reason: String
)

// Activity DTOs
data class ActivityEventDto(
    val id: String,
    val projectId: String,
    val userId: String,
    val timestamp: Long,
    val type: String,
    val data: Map<String, Any>
)

// File DTOs
data class SharedFileDto(
    val id: String,
    val projectId: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String,
    val downloadUrl: String,
    val uploadedBy: String,
    val uploadedAt: Long,
    val isArchived: Boolean
)

data class FileUploadResponse(
    val id: String,
    val downloadUrl: String,
    val message: String
)

// Presence DTOs
data class PresenceDto(
    val userId: String,
    val userName: String,
    val isOnline: Boolean,
    val lastSeen: Long,
    val currentProjectId: String?
)

// Generic response wrapper
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String,
    val code: Int
)

// Extension functions for domain mapping
fun ProjectDto.toDomain() = com.syncup.domain.model.Project(
    id = id,
    name = name,
    description = description,
    owner = owner,
    members = emptyList(),
    coverColor = coverColor,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isArchived = isArchived
)

fun TaskDto.toDomain() = com.syncup.domain.model.Task(
    id = id,
    projectId = projectId,
    milestoneId = milestoneId,
    title = title,
    description = description ?: "",
    assignedTo = assignedTo,
    dueDate = dueDate,
    status = com.syncup.domain.model.TaskStatus.valueOf(status),
    priority = com.syncup.domain.model.TaskPriority.valueOf(priority),
    labels = labels,
    attachments = attachments,
    blockerReason = blockerReason,
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt
)
