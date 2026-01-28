package com.syncup.domain.model

import java.util.UUID

/**
 * Domain model representing a user in SyncUp
 */
data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val name: String,
    val university: String,
    val profilePictureUrl: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Domain model for user authentication
 */
data class AuthCredentials(
    val email: String,
    val password: String?,
    val provider: AuthProvider = AuthProvider.EMAIL
)

enum class AuthProvider {
    EMAIL, GOOGLE, APPLE
}

/**
 * Domain model representing authentication state
 */
sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}
