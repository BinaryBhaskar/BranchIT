package com.binarybhaskar.branchit

import UserProfileCard
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.binarybhaskar.branchit.screens.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import com.binarybhaskar.branchit.repository.UserRepository
import com.binarybhaskar.branchit.model.UserProfile

enum class MainScreen(val label: String) {
    Home("Home"),
    Connect("Connect"),
    Post("Post"),
    Chat("Chat"),
    Profile("Profile"),
    Updates("Updates"), // Hidden, only for notification
    Settings("Settings") // Hidden, only for settings
}

// Multiplatform device size detection
@Composable
expect fun isLargeScreen(): Boolean

private val screenIcons = mapOf(
    MainScreen.Home to "\uD83C\uDFE0",      // House
    MainScreen.Connect to "\uD83D\uDC65",   // People
    MainScreen.Post to "✏️",                // Pencil
    MainScreen.Chat to "\uD83D\uDCAC",      // Chat bubble
    MainScreen.Profile to "\uD83D\uDC64"    // Person
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    // Observe login state from repository
    val userState by UserRepository.currentUser.collectAsState()
    var selectedScreen by rememberSaveable { mutableStateOf(MainScreen.Home) }

    if (userState == null) {
        LoginScreen(
            isLoading = false,
            onGoogleLogin = {
                // For MVP: use dummy profile, replace with real Google login
                UserRepository.loginWithGoogle(
                    UserProfile(
                        uid = "demoUid",
                        username = "ggvstudent",
                        displayName = "GGV Student",
                        ggvInfo = "B.Tech CSE 2025",
                        email = "student@ggv.edu.in"
                    )
                )
            }
        )
    } else {
        val largeScreen = isLargeScreen()
        val navScreens = listOf(
            MainScreen.Home,
            MainScreen.Connect,
            MainScreen.Post,
            MainScreen.Chat,
            MainScreen.Profile
        )
        val showUpdates = selectedScreen == MainScreen.Updates
        val showTopBar = selectedScreen != MainScreen.Updates
        if (largeScreen) {
            Row(Modifier.fillMaxSize()) {
                NavigationRail {
                    navScreens.forEach { screen ->
                        NavigationRailItem(
                            selected = selectedScreen == screen,
                            onClick = { selectedScreen = screen },
                            label = { Text(screen.label) },
                            icon = { Text(screenIcons[screen] ?: "") }
                        )
                    }
                }
                Column(Modifier.weight(1f)) {
                    if (showTopBar) {
                        TopAppBar(
                            title = { Text("BranchIT") },
                            actions = {
                                IconButton(onClick = { selectedScreen = MainScreen.Updates }) {
                                    Text("🔔", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                                }
                            }
                        )
                    }
                    Box(Modifier.fillMaxSize()) {
                        if (showUpdates) {
                            UpdatesScreen()
                        } else {
                            MainScreenContent(selectedScreen) {
                                selectedScreen = MainScreen.Settings
                            }
                        }
                    }
                }
            }
        } else {
            Scaffold(
                topBar = {
                    if (showTopBar) {
                        TopAppBar(
                            title = { Text("BranchIT") },
                            actions = {
                                IconButton(onClick = { selectedScreen = MainScreen.Updates }) {
                                    Text("🔔", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                                }
                            }
                        )
                    }
                },
                bottomBar = {
                    NavigationBar {
                        navScreens.forEach { screen ->
                            NavigationBarItem(
                                selected = selectedScreen == screen,
                                onClick = { selectedScreen = screen },
                                label = { Text(screen.label) },
                                icon = { Text(screenIcons[screen] ?: "") }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(Modifier.padding(innerPadding).fillMaxSize()) {
                    if (showUpdates) {
                        UpdatesScreen()
                    } else {
                        MainScreenContent(selectedScreen) {
                            selectedScreen = MainScreen.Settings
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenContent(screen: MainScreen, onEditProfile: () -> Unit = {}) {
    when (screen) {
        MainScreen.Home -> HomeScreen()
        MainScreen.Connect -> ConnectScreen()
        MainScreen.Post -> PostScreen()
        MainScreen.Chat -> ChatScreen()
        MainScreen.Profile -> {
            val user = UserRepository.currentUser.collectAsState().value
            if (user != null) {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(24.dp))
                    UserProfileCard(user = user)
                }
            } else {
                CircularProgressIndicator(Modifier)
            }
        }
        MainScreen.Settings -> SettingsScreen()
        else -> {}
    }
}