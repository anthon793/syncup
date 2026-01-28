package com.syncup.data.repository

import com.syncup.data.local.dao.UserDao
import com.syncup.data.local.entity.UserEntity
import com.syncup.data.remote.SyncUpApi
import com.syncup.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserRepository {
    suspend fun signUp(email: String, password: String, name: String, university: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun getUserById(userId: String): Result<User>
    fun observeUserById(userId: String): Flow<User?>
    suspend fun updateUserOnlineStatus(userId: String, isOnline: Boolean): Result<Unit>
    fun observeOnlineUsers(): Flow<List<User>>
    fun observeUsersByUniversity(university: String): Flow<List<User>>
}

class UserRepositoryImpl(
    private val api: SyncUpApi,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun signUp(email: String, password: String, name: String, university: String): Result<User> {
        return try {
            val response = api.signUp(
                com.syncup.data.remote.dto.SignUpRequest(email, password, name, university)
            )
            if (response.success && response.user != null) {
                val userEntity = response.user.toEntity()
                userDao.insertUser(userEntity)
                Result.success(userEntity.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(
                com.syncup.data.remote.dto.LoginRequest(email, password)
            )
            if (response.user != null) {
                val userEntity = response.user.toEntity()
                userDao.insertUser(userEntity)
                Result.success(userEntity.toDomain())
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val user = api.getUserById(userId)
            val userEntity = user.toEntity()
            userDao.insertUser(userEntity)
            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeUserById(userId: String): Flow<User?> {
        return userDao.observeUserById(userId).map { it?.toDomain() }
    }

    override suspend fun updateUserOnlineStatus(userId: String, isOnline: Boolean): Result<Unit> {
        return try {
            val user = userDao.getUserById(userId) ?: return Result.failure(Exception("User not found"))
            val updated = user.copy(isOnline = isOnline, lastSeen = System.currentTimeMillis())
            userDao.updateUser(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeOnlineUsers(): Flow<List<User>> {
        return userDao.observeOnlineUsers().map { users ->
            users.map { it.toDomain() }
        }
    }

    override fun observeUsersByUniversity(university: String): Flow<List<User>> {
        return userDao.observeUsersByUniversity(university).map { users ->
            users.map { it.toDomain() }
        }
    }
}

// Extension functions for mapping
fun com.syncup.data.remote.dto.UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        name = name,
        university = university,
        profilePictureUrl = profilePictureUrl,
        isOnline = isOnline,
        lastSeen = lastSeen,
        createdAt = createdAt
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        university = university,
        profilePictureUrl = profilePictureUrl,
        isOnline = isOnline,
        lastSeen = lastSeen,
        createdAt = createdAt
    )
}
