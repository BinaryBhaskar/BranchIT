package com.binarybhaskar.branchit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.binarybhaskar.branchit.repository.UserRepository

@Composable
fun ProfileScreen(onEditProfile: () -> Unit) {
    val user = UserRepository.currentUser.collectAsState().value ?: return

    Column(Modifier.padding(24.dp)) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        ProfileField(label = "Display Name", value = user.displayName)
        ProfileField(label = "Username", value = user.username)
        ProfileField(label = "GGV Info", value = user.ggvInfo)
        ProfileField(label = "About", value = user.about)
        ProfileField(label = "LinkedIn", value = user.linkedIn)
        ProfileField(label = "GitHub", value = user.github)
        ProfileField(label = "Instagram", value = user.instagram)
        ProfileField(label = "Email", value = user.email)
        Spacer(Modifier.height(16.dp))
        Text("Badges", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.padding(vertical = 8.dp)) {
            if (user.isVerified) Badge(text = "Verified")
            if (user.skills.isNotEmpty()) Badge(text = "Skilled")
            if (user.projects.size == 3) Badge(text = "Project Pro")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onEditProfile() }) {
            Text("Edit Profile")
        }
    }
}

@Composable
fun Badge(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
