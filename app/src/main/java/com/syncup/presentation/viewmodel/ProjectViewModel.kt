package com.syncup.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syncup.data.repository.ProjectRepository
import com.syncup.data.repository.TaskRepository
import com.syncup.domain.model.Project
import com.syncup.domain.model.Task
import com.syncup.domain.model.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _currentProject = MutableStateFlow<Project?>(null)
    val currentProject: StateFlow<Project?> = _currentProject.asStateFlow()

    private val _projectTasks = MutableStateFlow<List<Task>>(emptyList())
    val projectTasks: StateFlow<List<Task>> = _projectTasks.asStateFlow()

    private val _todoTasks = MutableStateFlow<List<Task>>(emptyList())
    val todoTasks: StateFlow<List<Task>> = _todoTasks.asStateFlow()

    private val _inProgressTasks = MutableStateFlow<List<Task>>(emptyList())
    val inProgressTasks: StateFlow<List<Task>> = _inProgressTasks.asStateFlow()

    private val _criticalTasks = MutableStateFlow<List<Task>>(emptyList())
    val criticalTasks: StateFlow<List<Task>> = _criticalTasks.asStateFlow()

    private val _doneTasks = MutableStateFlow<List<Task>>(emptyList())
    val doneTasks: StateFlow<List<Task>> = _doneTasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadProject(projectId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                projectRepository.getProjectById(projectId).onSuccess { project ->
                    _currentProject.value = project
                    loadProjectTasks(projectId)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadProjectTasks(projectId: String) {
        viewModelScope.launch {
            taskRepository.getProjectTasks(projectId).onSuccess { tasks ->
                _projectTasks.value = tasks

                // Separate tasks by status for Kanban view
                _todoTasks.value = tasks.filter { it.status == TaskStatus.TODO }
                _inProgressTasks.value = tasks.filter { it.status == TaskStatus.IN_PROGRESS }
                _criticalTasks.value = tasks.filter { it.status == TaskStatus.CRITICAL }
                _doneTasks.value = tasks.filter { it.status == TaskStatus.DONE }
            }
        }
    }

    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        viewModelScope.launch {
            taskRepository.updateTaskStatus(taskId, newStatus).onSuccess {
                // Update local state
                val updatedTasks = _projectTasks.value.map { task ->
                    if (task.id == taskId) task.copy(status = newStatus) else task
                }
                _projectTasks.value = updatedTasks

                // Reorganize by status
                _todoTasks.value = updatedTasks.filter { it.status == TaskStatus.TODO }
                _inProgressTasks.value = updatedTasks.filter { it.status == TaskStatus.IN_PROGRESS }
                _criticalTasks.value = updatedTasks.filter { it.status == TaskStatus.CRITICAL }
                _doneTasks.value = updatedTasks.filter { it.status == TaskStatus.DONE }
            }
        }
    }

    fun flagBlocker(taskId: String, reason: String) {
        viewModelScope.launch {
            taskRepository.flagBlocker(taskId, reason).onSuccess {
                // Update task to CRITICAL status
                updateTaskStatus(taskId, TaskStatus.CRITICAL)
            }
        }
    }

    fun resolveBlocker(taskId: String) {
        viewModelScope.launch {
            taskRepository.resolveBlocker(taskId).onSuccess {
                // Refresh tasks to get updated state
                _currentProject.value?.id?.let { loadProjectTasks(it) }
            }
        }
    }

    fun createTask(title: String, description: String, dueDate: Long, assignedTo: String? = null) {
        viewModelScope.launch {
            val project = _currentProject.value ?: return@launch
            val task = Task(
                projectId = project.id,
                title = title,
                description = description,
                dueDate = dueDate,
                assignedTo = assignedTo,
                createdBy = "" // Current user ID
            )
            taskRepository.createTask(task).onSuccess {
                loadProjectTasks(project.id)
            }
        }
    }
}
