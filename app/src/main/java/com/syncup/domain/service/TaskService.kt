package com.syncup.domain.service

import com.syncup.domain.model.*

/**
 * Task management service with assignment and status update logic
 */
class TaskService {
    
    /**
     * Simulate task assignment based on user role and availability
     */
    fun assignTask(task: Task, assigneeId: String, assignedBy: RoleBasedUser): Result<Task> {
        return if (assignedBy.canAssignTasks()) {
            val updatedTask = task.copy(assignedTo = assigneeId)
            Result.success(updatedTask)
        } else {
            Result.failure(Exception("User doesn't have permission to assign tasks"))
        }
    }
    
    /**
     * Update task status with validation
     */
    fun updateTaskStatus(
        task: Task,
        newStatus: TaskStatus,
        updatedBy: RoleBasedUser
    ): Result<Task> {
        return if (updatedBy.canUpdateTaskStatus() && 
                  (task.assignedTo == updatedBy.id || updatedBy.role == UserRole.ADMIN)) {
            val updatedTask = task.copy(status = newStatus)
            Result.success(updatedTask)
        } else {
            Result.failure(Exception("User cannot update this task"))
        }
    }
    
    /**
     * Get all tasks for a user
     */
    fun getUserTasks(userId: String, tasks: List<Task>): List<Task> {
        return tasks.filter { it.assignedTo == userId }
    }
    
    /**
     * Get tasks filtered by status
     */
    fun getTasksByStatus(tasks: List<Task>, status: TaskStatus): List<Task> {
        return tasks.filter { it.status == status }
    }
}
