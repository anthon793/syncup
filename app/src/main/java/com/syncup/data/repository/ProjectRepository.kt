package com.syncup.data.repository

import com.syncup.data.local.dao.ProjectDao
import com.syncup.data.local.dao.TaskDao
import com.syncup.data.local.entity.ProjectEntity
import com.syncup.data.local.entity.TaskEntity
import com.syncup.data.remote.SyncUpApi
import com.syncup.data.remote.dto.*
import com.syncup.domain.model.Project
import com.syncup.domain.model.Task
import com.syncup.domain.model.TaskPriority
import com.syncup.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProjectRepository {
    suspend fun getProjects(): Result<List<Project>>
    fun observeActiveProjects(): Flow<List<Project>>
    suspend fun getProjectById(projectId: String): Result<Project>
    fun observeProjectById(projectId: String): Flow<Project?>
    suspend fun createProject(name: String, description: String, coverColor: String = "#6200EE"): Result<Project>
    suspend fun updateProject(projectId: String, name: String, description: String): Result<Project>
    suspend fun addMemberToProject(projectId: String, userId: String): Result<Unit>
}

interface TaskRepository {
    suspend fun getProjectTasks(projectId: String): Result<List<Task>>
    fun observeProjectTasks(projectId: String): Flow<List<Task>>
    fun observeTasksByStatus(projectId: String, status: TaskStatus): Flow<List<Task>>
    suspend fun getTaskById(taskId: String): Result<Task>
    suspend fun createTask(task: Task): Result<Task>
    suspend fun updateTaskStatus(taskId: String, status: TaskStatus): Result<Task>
    suspend fun flagBlocker(taskId: String, reason: String): Result<Unit>
    suspend fun resolveBlocker(taskId: String): Result<Unit>
    fun observeHighestPriorityTask(): Flow<Task?>
}

class ProjectRepositoryImpl(
    private val api: SyncUpApi,
    private val projectDao: ProjectDao
) : ProjectRepository {
    override suspend fun getProjects(): Result<List<Project>> {
        return try {
            val projects = api.getProjects()
            projects.forEach { dto ->
                projectDao.insertProject(dto.toEntity())
            }
            Result.success(projects.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeActiveProjects(): Flow<List<Project>> {
        return projectDao.observeActiveProjects().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProjectById(projectId: String): Result<Project> {
        return try {
            val project = api.getProjectById(projectId)
            projectDao.insertProject(project.toEntity())
            Result.success(project.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeProjectById(projectId: String): Flow<Project?> {
        return projectDao.observeProjectById(projectId).map { it?.toDomain() }
    }

    override suspend fun createProject(name: String, description: String, coverColor: String): Result<Project> {
        return try {
            val request = com.syncup.data.remote.dto.CreateProjectRequest(name, description, coverColor)
            val project = api.createProject(request)
            projectDao.insertProject(project.toEntity())
            Result.success(project.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProject(projectId: String, name: String, description: String): Result<Project> {
        return try {
            val request = com.syncup.data.remote.dto.CreateProjectRequest(name, description)
            val project = api.updateProject(projectId, request)
            projectDao.insertProject(project.toEntity())
            Result.success(project.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addMemberToProject(projectId: String, userId: String): Result<Unit> {
        return try {
            api.addMember(projectId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class TaskRepositoryImpl(
    private val api: SyncUpApi,
    private val taskDao: TaskDao
) : TaskRepository {
    override suspend fun getProjectTasks(projectId: String): Result<List<Task>> {
        return try {
            val tasks = api.getProjectTasks(projectId)
            tasks.forEach { dto ->
                taskDao.insertTask(dto.toEntity())
            }
            Result.success(tasks.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeProjectTasks(projectId: String): Flow<List<Task>> {
        return taskDao.observeProjectTasks(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeTasksByStatus(projectId: String, status: TaskStatus): Flow<List<Task>> {
        return taskDao.observeTasksByStatus(projectId, status.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(taskId: String): Result<Task> {
        return try {
            val task = api.getTaskById(taskId)
            taskDao.insertTask(task.toEntity())
            Result.success(task.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTask(task: Task): Result<Task> {
        return try {
            val request = com.syncup.data.remote.dto.CreateTaskRequest(
                projectId = task.projectId,
                milestoneId = task.milestoneId,
                title = task.title,
                description = task.description,
                assignedTo = task.assignedTo,
                dueDate = task.dueDate ?: System.currentTimeMillis(),
                priority = task.priority.name
            )
            val createdTask = api.createTask(request)
            taskDao.insertTask(createdTask.toEntity())
            Result.success(createdTask.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTaskStatus(taskId: String, status: TaskStatus): Result<Task> {
        return try {
            val request = com.syncup.data.remote.dto.UpdateTaskStatusRequest(taskId, status.name)
            val updatedTask = api.updateTaskStatus(taskId, request)
            taskDao.insertTask(updatedTask.toEntity())
            Result.success(updatedTask.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun flagBlocker(taskId: String, reason: String): Result<Unit> {
        return try {
            val request = com.syncup.data.remote.dto.FlagBlockerRequest(taskId, reason)
            api.flagBlocker(taskId, request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resolveBlocker(taskId: String): Result<Unit> {
        return try {
            api.resolveBlocker(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeHighestPriorityTask(): Flow<Task?> {
        return taskDao.observeHighestPriorityTask().map { it?.toDomain() }
    }
}

// Extension functions for mapping
fun com.syncup.data.remote.dto.ProjectDto.toEntity(): ProjectEntity {
    return ProjectEntity(
        id = id,
        name = name,
        description = description,
        owner = owner,
        memberIds = memberIds.joinToString(","),
        coverColor = coverColor,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isArchived = isArchived
    )
}

fun ProjectEntity.toDomain(): Project {
    return Project(
        id = id,
        name = name,
        description = description,
        owner = owner,
        members = if (memberIds.isNotEmpty()) memberIds.split(",") else emptyList(),
        coverColor = coverColor,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isArchived = isArchived
    )
}

fun com.syncup.data.remote.dto.TaskDto.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        projectId = projectId,
        milestoneId = milestoneId,
        title = title,
        description = description,
        assignedTo = assignedTo,
        dueDate = dueDate,
        status = status,
        priority = priority,
        labels = labels.joinToString(","),
        attachments = attachments.joinToString(","),
        blockerReason = blockerReason,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        projectId = projectId,
        milestoneId = milestoneId,
        title = title,
        description = description,
        assignedTo = assignedTo,
        dueDate = dueDate,
        status = TaskStatus.valueOf(status),
        priority = TaskPriority.valueOf(priority),
        labels = if (labels.isNotEmpty()) labels.split(",") else emptyList(),
        attachments = if (attachments.isNotEmpty()) attachments.split(",") else emptyList(),
        blockerReason = blockerReason,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
