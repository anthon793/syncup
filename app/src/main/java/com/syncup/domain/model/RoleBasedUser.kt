package com.syncup.domain.model

/**
 * User role enumeration for role-based access control
 */
enum class UserRole {
    ADMIN,      // Project lead - can manage everything
    MEMBER,     // Team member - can view and update assigned tasks
    VIEWER      // Read-only access
}

/**
 * Extended user model with role-based functionality
 */
data class RoleBasedUser(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val joinDate: Long = System.currentTimeMillis()
) {
    fun canCreateProjects(): Boolean = role == UserRole.ADMIN
    fun canAssignTasks(): Boolean = role == UserRole.ADMIN
    fun canDefineMilestones(): Boolean = role == UserRole.ADMIN
    fun canUpdateTaskStatus(): Boolean = role in listOf(UserRole.ADMIN, UserRole.MEMBER)
    fun canSendMessages(): Boolean = role in listOf(UserRole.ADMIN, UserRole.MEMBER)
}
