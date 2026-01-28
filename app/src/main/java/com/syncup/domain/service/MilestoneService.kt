package com.syncup.domain.service

import com.syncup.domain.model.*

/**
 * Milestone tracking service with automatic completion calculation
 */
class MilestoneService {
    
    /**
     * Calculate milestone completion percentage based on associated tasks
     */
    fun calculateMilestoneProgress(
        milestone: Milestone,
        allTasks: List<Task>
    ): Float {
        val associatedTasks = allTasks.filter { it.milestoneId == milestone.id }
        
        if (associatedTasks.isEmpty()) return 0f
        
        val completedTasks = associatedTasks.count { it.status == TaskStatus.DONE }
        return completedTasks.toFloat() / associatedTasks.size
    }
    
    /**
     * Check if milestone is automatically completed
     */
    fun isMilestoneCompleted(
        milestone: Milestone,
        allTasks: List<Task>
    ): Boolean {
        val associatedTasks = allTasks.filter { it.milestoneId == milestone.id }
        
        if (associatedTasks.isEmpty()) return false
        
        return associatedTasks.all { it.status == TaskStatus.DONE }
    }
    
    /**
     * Get milestone status with detailed breakdown
     */
    fun getMilestoneStatus(
        milestone: Milestone,
        allTasks: List<Task>
    ): MilestoneStatus {
        val associatedTasks = allTasks.filter { it.milestoneId == milestone.id }
        val todoCount = associatedTasks.count { it.status == TaskStatus.TODO }
        val inProgressCount = associatedTasks.count { it.status == TaskStatus.IN_PROGRESS }
        val doneCount = associatedTasks.count { it.status == TaskStatus.DONE }
        val isCompleted = isMilestoneCompleted(milestone, allTasks)
        val progress = calculateMilestoneProgress(milestone, allTasks)
        
        return MilestoneStatus(
            milestoneId = milestone.id,
            title = milestone.title,
            totalTasks = associatedTasks.size,
            todoCount = todoCount,
            inProgressCount = inProgressCount,
            doneCount = doneCount,
            progress = progress,
            isCompleted = isCompleted,
            dueDate = milestone.dueDate
        )
    }
}

data class MilestoneStatus(
    val milestoneId: String,
    val title: String,
    val totalTasks: Int,
    val todoCount: Int,
    val inProgressCount: Int,
    val doneCount: Int,
    val progress: Float,
    val isCompleted: Boolean,
    val dueDate: Long
)
