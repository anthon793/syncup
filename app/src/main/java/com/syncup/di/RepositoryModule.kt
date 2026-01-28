package com.syncup.di

import com.syncup.data.local.dao.ProjectDao
import com.syncup.data.local.dao.TaskDao
import com.syncup.data.local.dao.UserDao
import com.syncup.data.remote.SyncUpApi
import com.syncup.data.repository.ProjectRepository
import com.syncup.data.repository.ProjectRepositoryImpl
import com.syncup.data.repository.TaskRepository
import com.syncup.data.repository.TaskRepositoryImpl
import com.syncup.data.repository.UserRepository
import com.syncup.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideUserRepository(api: SyncUpApi, userDao: UserDao): UserRepository {
        return UserRepositoryImpl(api, userDao)
    }

    @Singleton
    @Provides
    fun provideProjectRepository(api: SyncUpApi, projectDao: ProjectDao): ProjectRepository {
        return ProjectRepositoryImpl(api, projectDao)
    }

    @Singleton
    @Provides
    fun provideTaskRepository(api: SyncUpApi, taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(api, taskDao)
    }
}
