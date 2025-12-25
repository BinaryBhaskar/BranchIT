package com.binarybhaskar.branchit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binarybhaskar.branchit.repository.UserRepository

@Composable
fun ProfileScreen(onEditProfile: () -> Unit) {
    val user = UserRepository.currentUser.collectAsState().value ?: return
    Column(
        modifier = Modifier.fillMaxSize().padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Profile Header ---
        Box {
            // Background image (MVP: placeholder color)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.BottomCenter
            ) {
                // TODO: Load user.backgroundUrl if available
                if (user.backgroundUrl.isBlank()) {
                    Text("Background", color = Color.Gray, modifier = Modifier.padding(8.dp))
                } else {
                    // Load image from user.backgroundUrl (future)
                }
            }
            // Profile picture (overlapping)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 60.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBDBDBD)),
                contentAlignment = Alignment.Center
            ) {
                // TODO: Load user.profilePicUrl if available
                Text(user.displayName.take(1), fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(70.dp))
        // Name + branch + verification
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(user.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            if (user.isVerified) {
                Spacer(Modifier.width(6.dp))
                Badge(text = "✔ Verified")
            }
        }
        Text("@${user.username} • ${user.ggvInfo}", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        // Profile strength meter (bonus, MVP: simple bar)
        ProfileStrengthMeter(user)
        Spacer(Modifier.height(8.dp))
        // About
        Text(user.about, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(Modifier.height(12.dp))
        // Skills (chips)
        if (user.skills.isNotEmpty()) {
            Text("Skills", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.Center) {
                user.skills.forEach { skill ->
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Text(skill, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
        // Projects (exactly 3)
        if (user.projects.isNotEmpty()) {
            Text("Projects", style = MaterialTheme.typography.titleMedium)
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                user.projects.forEachIndexed { idx, project ->
                    Text("${idx+1}. ${project}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        // Achievements
        if (user.achievements.isNotEmpty()) {
            Text("Achievements", style = MaterialTheme.typography.titleMedium)
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                user.achievements.forEach { ach ->
                    Text("• $ach", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        // Social Links
        Text("Social Links", style = MaterialTheme.typography.titleMedium)
        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            if (user.email.isNotBlank()) ProfileField(label = "Email", value = user.email)
            if (user.linkedIn.isNotBlank()) ProfileField(label = "LinkedIn", value = user.linkedIn)
            if (user.github.isNotBlank()) ProfileField(label = "GitHub", value = user.github)
            if (user.instagram.isNotBlank()) ProfileField(label = "Instagram", value = user.instagram)
        }
        // Resume upload (MVP: show if uploaded)
        if (user.resumeUrl.isNotBlank()) {
            Text("Resume Uploaded", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.height(16.dp))
        // Gamification badges (bonus)
        // Commented out: gamificationBadges not in UserProfile model
        // if (user.gamificationBadges.isNotEmpty()) {
        //     Text("Badges", style = MaterialTheme.typography.titleMedium)
        //     Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        //         user.gamificationBadges.forEach { badge ->
        //             Badge(text = badge)
        //         }
        //     }
        // }
        // View count & share (bonus)
        // Commented out: viewCount not in UserProfile model
        // Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        //     if (user.viewCount > 0) {
        //         Text("👁 ${user.viewCount}", color = Color.Gray, modifier = Modifier.padding(end = 12.dp))
        //     }
        //     Button(onClick = { /* TODO: Share profile link */ }, colors = ButtonDefaults.outlinedButtonColors()) {
        //         Text("Share Profile")
        //     }
        // }
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

@Composable
fun ProfileStrengthMeter(user: com.binarybhaskar.branchit.model.UserProfile) {
    // MVP: simple progress bar based on filled fields
    val total = 8 // displayName, username, about, skills, projects, achievements, social, resume
    var score = 0
    if (user.displayName.isNotBlank()) score++
    if (user.username.isNotBlank()) score++
    if (user.about.isNotBlank()) score++
    if (user.skills.isNotEmpty()) score++
    if (user.projects.isNotEmpty()) score++
    if (user.achievements.isNotEmpty()) score++
    if (user.email.isNotBlank() || user.linkedIn.isNotBlank() || user.github.isNotBlank() || user.instagram.isNotBlank()) score++
    if (user.resumeUrl.isNotBlank()) score++
    val percent = score / total.toFloat()
    LinearProgressIndicator(progress = { percent }, modifier = Modifier.fillMaxWidth(0.7f).height(8.dp).clip(MaterialTheme.shapes.small))
    Text("Profile Strength: ${(percent * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
}
