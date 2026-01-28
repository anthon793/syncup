package com.syncup.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.syncup.data.local.dao.ActivityFeedDao
import com.syncup.data.local.dao.ProjectDao
import com.syncup.data.local.dao.TaskDao
import com.syncup.data.local.dao.UserDao
import com.syncup.data.local.entity.ActivityFeedEntity
import com.syncup.data.local.entity.MilestoneEntity
import com.syncup.data.local.entity.PresenceInfoEntity
import com.syncup.data.local.entity.ProjectEntity
import com.syncup.data.local.entity.ResourceLinkEntity
import com.syncup.data.local.entity.SharedFileEntity
import com.syncup.data.local.entity.TaskEntity
import com.syncup.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProjectEntity::class,
        TaskEntity::class,
        MilestoneEntity::class,
        ActivityFeedEntity::class,
        SharedFileEntity::class,
        ResourceLinkEntity::class,
        PresenceInfoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SyncUpDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun activityFeedDao(): ActivityFeedDao

    companion object {
        private var instance: SyncUpDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): SyncUpDatabase {
            if (instance == null) {
                synchronized(LOCK) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            SyncUpDatabase::class.java,
                            "syncup_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}
