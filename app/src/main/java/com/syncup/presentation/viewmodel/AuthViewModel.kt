package com.syncup.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syncup.data.repository.UserRepository
import com.syncup.domain.model.AuthState
import com.syncup.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Mock user for demo purposes
    private val mockUser = User(
        id = "user_1",
        email = "alex.student@university.edu",
        name = "Alex Student",
        university = "Tech University",
        profilePictureUrl = null,
        isOnline = true,
        lastSeen = System.currentTimeMillis()
    )

    fun signup(email: String, password: String, name: String, university: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Use mock data - simulate network delay
            delay(500)
            _authState.value = AuthState.Authenticated(
                mockUser.copy(email = email, name = name, university = university)
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Simulate network delay
            delay(500)
            
            // Validate admin credentials
            if (email == "admin@gmail.com" && password == "admin123") {
                _authState.value = AuthState.Authenticated(
                    mockUser.copy(
                        email = email,
                        name = "Admin User"
                    )
                )
            } else {
                _authState.value = AuthState.Error("Invalid email or password. Please try again.")
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
    }
}
