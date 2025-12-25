package com.binarybhaskar.branchit.model

/**
 * Data model for project details.
 */
data class Project(
    val title: String = "",
    val description: String = "",
    val link: String = ""
)

/**
 * Data model for user profile, mapped to Firestore and used in app logic.
 */
data class UserProfile(
    val uid: String = "",
    val username: String = "",
    val displayName: String = "",
    val ggvInfo: String = "",
    val backgroundUrl: String = "",
    val about: String = "",
    val linkedIn: String = "",
    val github: String = "",
    val instagram: String = "",
    val email: String = "",
    val skills: List<String> = emptyList(),
    val resumeUrl: String = "",
    val projects: List<Project> = emptyList(), // Exactly 3
    val achievements: List<String> = emptyList(),
    val isVerified: Boolean = false
)