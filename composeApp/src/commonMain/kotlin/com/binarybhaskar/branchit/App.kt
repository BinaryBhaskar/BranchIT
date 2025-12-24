package com.binarybhaskar.branchit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.binarybhaskar.branchit.screens.*

enum class MainScreen(val label: String) {
    Home("Home"),
    Connect("Connect"),
    Post("Post"),
    Updates("Updates"),
    Profile("Profile")
}

// Multiplatform device size detection
@Composable
expect fun isLargeScreen(): Boolean

private val screenIcons = mapOf(
    MainScreen.Home to "\uD83C\uDFE0",      // House
    MainScreen.Connect to "\uD83D\uDC65",   // People
    MainScreen.Post to "✏️",                // Pencil
    MainScreen.Updates to "\uD83D\uDCC8",  // Chart
    MainScreen.Profile to "\uD83D\uDC64"   // Person
)

@Composable
@Preview
fun App() {
    // Simulate login state (replace with persistent logic as needed)
    var isLoggedIn by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf(MainScreen.Home) }

    if (!isLoggedIn) {
        LoginScreen(
            isLoading = false,
            onGoogleLogin = { isLoggedIn = true }
        )
    } else {
        val largeScreen = isLargeScreen()
        if (largeScreen) {
            Row(Modifier.fillMaxSize()) {
                NavigationRail {
                    MainScreen.entries.forEach { screen ->
                        NavigationRailItem(
                            selected = selectedScreen == screen,
                            onClick = { selectedScreen = screen },
                            label = { Text(screen.label) },
                            icon = { Text(screenIcons[screen] ?: "") }
                        )
                    }
                }
                Box(Modifier.weight(1f)) {
                    MainScreenContent(selectedScreen)
                }
            }
        } else {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        MainScreen.entries.forEach { screen ->
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
                    MainScreenContent(selectedScreen)
                }
            }
        }
    }
}

@Composable
fun MainScreenContent(screen: MainScreen) {
    when (screen) {
        MainScreen.Home -> HomeScreen()
        MainScreen.Connect -> ConnectScreen()
        MainScreen.Post -> PostScreen()
        MainScreen.Updates -> UpdatesScreen()
        MainScreen.Profile -> ProfileScreen()
    }
}