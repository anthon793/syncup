package com.syncup.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syncup.data.repository.ProjectRepository
import com.syncup.data.repository.TaskRepository
import com.syncup.data.repository.UserRepository
import com.syncup.domain.model.ActivityFeedItem
import com.syncup.domain.model.DeadlineInfo
import com.syncup.domain.model.Project
import com.syncup.domain.model.RiskLevel
import com.syncup.domain.model.Task
import com.syncup.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HubViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _onlineUsers = MutableStateFlow<List<User>>(emptyList())
    val onlineUsers: StateFlow<List<User>> = _onlineUsers.asStateFlow()

    private val _highPriorityTask = MutableStateFlow<Task?>(null)
    val highPriorityTask: StateFlow<Task?> = _highPriorityTask.asStateFlow()

    private val _upcomingDeadlines = MutableStateFlow<List<DeadlineInfo>>(emptyList())
    val upcomingDeadlines: StateFlow<List<DeadlineInfo>> = _upcomingDeadlines.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadHubData()
    }

    private fun loadHubData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load projects
                projectRepository.getProjects().onSuccess { projects ->
                    _projects.value = projects
                }

                // Load online users
                userRepository.observeOnlineUsers().collect { users ->
                    _onlineUsers.value = users
                }

                // Load high priority task
                taskRepository.observeHighestPriorityTask().collect { task ->
                    _highPriorityTask.value = task
                }

                // Calculate upcoming deadlines
                calculateUpcomingDeadlines()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateUpcomingDeadlines() {
        // This will be called periodically or when tasks change
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val deadlines = mutableListOf<DeadlineInfo>()

            // Aggregate all tasks and sort by due date
            val allTasks = _projects.value.flatMap { project ->
                // In a real implementation, we'd query tasks for each project
                emptyList<Task>()
            }

            allTasks.filter { it.dueDate != null && it.dueDate > currentTime }
                .sortedBy { it.dueDate ?: Long.MAX_VALUE }
                .take(5) // Show top 5 upcoming deadlines
                .forEach { task ->
                    val timeRemaining = (task.dueDate ?: currentTime) - currentTime
                    val riskLevel = when {
                        timeRemaining < 86400000 -> RiskLevel.CRITICAL // < 1 day
                        timeRemaining < 259200000 -> RiskLevel.WARNING // < 3 days
                        else -> RiskLevel.NORMAL
                    }

                    deadlines.add(
                        DeadlineInfo(
                            taskId = task.id,
                            taskTitle = task.title,
                            dueDate = task.dueDate ?: 0L,
                            assignedUser = task.assignedTo ?: "",
                            riskLevel = riskLevel,
                            blockerActive = task.blockerReason != null,
                            timeRemainingMs = timeRemaining
                        )
                    )
                }

            _upcomingDeadlines.value = deadlines
        }
    }

    fun refreshHub() {
        loadHubData()
    }
}
