package com.syncup.di

import android.content.Context
import com.syncup.data.local.SyncUpDatabase
import com.syncup.data.local.dao.ActivityFeedDao
import com.syncup.data.local.dao.ProjectDao
import com.syncup.data.local.dao.TaskDao
import com.syncup.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideSyncUpDatabase(@ApplicationContext context: Context): SyncUpDatabase {
        return SyncUpDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(database: SyncUpDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideProjectDao(database: SyncUpDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: SyncUpDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideActivityFeedDao(database: SyncUpDatabase): ActivityFeedDao {
        return database.activityFeedDao()
    }
}
