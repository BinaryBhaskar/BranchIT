package com.binarybhaskar.branchit.repository

import com.binarybhaskar.branchit.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * MVP: Simple in-memory user repository. Replace with Firebase logic for production.
 */
object UserRepository {
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    fun isLoggedIn(): Boolean = _currentUser.value != null

    fun loginWithGoogle(dummyProfile: UserProfile) {
        // TODO: Replace with real Firebase Auth logic
        _currentUser.value = dummyProfile
    }

    fun logout() {
        _currentUser.value = null
    }

    fun updateProfile(updated: UserProfile) {
        _currentUser.value = updated
    }
}
