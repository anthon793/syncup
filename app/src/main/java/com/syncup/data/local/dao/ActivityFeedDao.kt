package com.syncup.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syncup.data.local.entity.ActivityFeedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityFeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityFeedEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityFeedEntity)

    @Query("SELECT * FROM activity_feed WHERE projectId = :projectId ORDER BY timestamp DESC LIMIT :limit")
    fun observeProjectActivity(projectId: String, limit: Int = 100): Flow<List<ActivityFeedEntity>>

    @Query("SELECT * FROM activity_feed ORDER BY timestamp DESC LIMIT :limit")
    fun observeGlobalActivity(limit: Int = 50): Flow<List<ActivityFeedEntity>>

    @Query("SELECT * FROM activity_feed WHERE userId = :userId AND projectId = :projectId ORDER BY timestamp DESC")
    fun observeUserProjectActivity(userId: String, projectId: String): Flow<List<ActivityFeedEntity>>

    @Query("SELECT * FROM activity_feed WHERE itemType = :type ORDER BY timestamp DESC LIMIT :limit")
    fun observeActivityByType(type: String, limit: Int = 50): Flow<List<ActivityFeedEntity>>

    @Query("DELETE FROM activity_feed WHERE projectId = :projectId")
    suspend fun deleteProjectActivity(projectId: String)

    @Query("DELETE FROM activity_feed WHERE timestamp < :before")
    suspend fun deleteOldActivity(before: Long)
}
