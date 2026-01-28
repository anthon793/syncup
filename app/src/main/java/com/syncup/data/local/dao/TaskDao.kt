package com.syncup.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.syncup.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY dueDate ASC")
    fun observeProjectTasks(projectId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND status = :status ORDER BY dueDate ASC")
    fun observeTasksByStatus(projectId: String, status: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE assignedTo = :userId AND projectId = :projectId ORDER BY dueDate ASC")
    fun observeUserTasks(userId: String, projectId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE milestoneId = :milestoneId ORDER BY dueDate ASC")
    fun observeMilestoneTasks(milestoneId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE status = 'CRITICAL' ORDER BY dueDate ASC LIMIT 1")
    fun observeHighestPriorityTask(): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE blockerReason IS NOT NULL AND projectId = :projectId")
    fun observeBlockedTasks(projectId: String): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE projectId = :projectId")
    suspend fun deleteProjectTasks(projectId: String)
}
