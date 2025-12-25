package com.binarybhaskar.branchit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

// --- Dummy Data Models (MVP) ---
data class ConnectUser(
    val id: String,
    val name: String,
    val username: String,
    val branch: String,
    val skills: List<String>,
    val type: String, // "Student", "Alumni", "Faculty"
    val isRecommended: Boolean = false,
    val isIncomingRequest: Boolean = false,
    val isOutgoingRequest: Boolean = false,
    val mutualConnections: Int = 0
)

private fun getDummyUsers(): List<ConnectUser> = listOf(
    ConnectUser("1", "Aditi Sharma", "aditi25", "CSE", listOf("Android", "Kotlin"), "Student", isRecommended = true, mutualConnections = 3),
    ConnectUser("2", "Rahul Verma", "rahulv", "ECE", listOf("Backend", "Firebase"), "Student", isOutgoingRequest = true, mutualConnections = 1),
    ConnectUser("3", "Dr. Meena Rao", "meenar", "CSE", listOf("AI", "ML"), "Faculty", isRecommended = true, mutualConnections = 5),
    ConnectUser("4", "Priya Singh", "priyasingh", "CSE", listOf("UI/UX", "Compose"), "Alumni", isIncomingRequest = true, mutualConnections = 2),
    ConnectUser("5", "Aman Gupta", "amangupta", "ECE", listOf("Flutter", "Firebase"), "Alumni", mutualConnections = 0)
)

@Composable
fun ConnectScreen() {
    var search by remember { mutableStateOf(TextFieldValue("")) }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Students", "Alumni", "Faculty")
    val users = getDummyUsers()
    val filtered = users.filter {
        (search.text.isBlank() ||
            it.name.contains(search.text, ignoreCase = true) ||
            it.username.contains(search.text, ignoreCase = true) ||
            it.skills.any { s -> s.contains(search.text, ignoreCase = true) }) &&
        it.type == tabs[selectedTab]
    }
    val clipboard = LocalClipboardManager.current
    val myProfileId = "ggvstudent" // For MVP, static

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Search by name, username, or skill") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        // Tabs
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { idx, label ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = { Text(label) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        // Sharable Profile ID
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Your Profile ID: ", style = MaterialTheme.typography.bodyMedium)
            Text(myProfileId, fontWeight = FontWeight.Bold)
            IconButton(onClick = { clipboard.setText(AnnotatedString(myProfileId)) }) {
                // Fallback for copy icon
                Text("Copy")
            }
        }
        Spacer(Modifier.height(8.dp))
        // Recommended Connections
        if (users.any { it.isRecommended && it.type == tabs[selectedTab] }) {
            Text("Recommended for you", fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.heightIn(max = 120.dp)) {
                items(users.filter { it.isRecommended && it.type == tabs[selectedTab] }) { user ->
                    ConnectUserCard(user)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        // Connection Requests
        if (users.any { (it.isIncomingRequest || it.isOutgoingRequest) && it.type == tabs[selectedTab] }) {
            Text("Connection Requests", fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.heightIn(max = 120.dp)) {
                items(users.filter { (it.isIncomingRequest || it.isOutgoingRequest) && it.type == tabs[selectedTab] }) { user ->
                    ConnectUserCard(user)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        // All Users
        Text("All ${tabs[selectedTab]}", fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(filtered) { user ->
                ConnectUserCard(user)
            }
        }
    }
}

@Composable
fun ConnectUserCard(user: ConnectUser) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Avatar (initials)
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString(""), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(user.name, fontWeight = FontWeight.Bold)
                Text("@${user.username} • ${user.branch}", style = MaterialTheme.typography.bodySmall)
                Text(user.skills.joinToString(", "), style = MaterialTheme.typography.bodySmall)
                if (user.mutualConnections > 0) {
                    Text("${user.mutualConnections} mutual connections", style = MaterialTheme.typography.labelSmall)
                }
            }
            if (user.isRecommended) {
                Text("⭐", fontSize = 18.sp)
            }
            if (user.isIncomingRequest) {
                Button(onClick = { /* Accept logic */ }, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Accept")
                }
            } else if (user.isOutgoingRequest) {
                OutlinedButton(onClick = { /* Cancel logic */ }, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Pending")
                }
            } else {
                OutlinedButton(onClick = { /* Connect logic */ }, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Connect")
                }
            }
        }
    }
}
