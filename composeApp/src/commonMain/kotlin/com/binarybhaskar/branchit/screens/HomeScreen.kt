package com.binarybhaskar.branchit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// --- MVP Data Model ---
data class Post(
    val id: String,
    val userAvatar: String?, // For MVP, use null or placeholder
    val userName: String,
    val userBranch: String,
    val contentText: String,
    val contentImage: String?, // For MVP, use null or placeholder
    val likeCount: Int,
    val commentCount: Int,
    val isConnection: Boolean,
    val isSameBranch: Boolean,
    val isTrending: Boolean = false,
    val gamification: String? = null // e.g., "🔥", "⭐"
)

// --- Dummy Feed Data (MVP) ---
private fun getDummyPosts(): List<Post> = listOf(
    Post(
        id = "1",
        userAvatar = null,
        userName = "Aditi Sharma",
        userBranch = "CSE 2025",
        contentText = "Excited to share my new project!",
        contentImage = null,
        likeCount = 12,
        commentCount = 3,
        isConnection = true,
        isSameBranch = true,
        isTrending = true,
        gamification = "🔥"
    ),
    Post(
        id = "2",
        userAvatar = null,
        userName = "Rahul Verma",
        userBranch = "ECE 2024",
        contentText = "Looking for a team for Hackathon!",
        contentImage = null,
        likeCount = 8,
        commentCount = 2,
        isConnection = false,
        isSameBranch = false,
        isTrending = false,
        gamification = null
    ),
    Post(
        id = "3",
        userAvatar = null,
        userName = "Priya Singh",
        userBranch = "CSE 2025",
        contentText = "Check out my new blog post on AI.",
        contentImage = null,
        likeCount = 15,
        commentCount = 5,
        isConnection = true,
        isSameBranch = true,
        isTrending = false,
        gamification = "⭐"
    )
)

@Composable
fun HomeScreen() {
    var isPublic by remember { mutableStateOf(true) }
    var posts by remember { mutableStateOf(getDummyPosts()) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(0.dp),
    ) {
        // Toggle Public/Private
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            SegmentedButton(
                options = listOf("All", "Following"),
                selectedIndex = if (isPublic) 0 else 1,
                onSelected = { isPublic = it == 0 }
            )
        }
        // Pull to refresh (MVP: refresh button)
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                coroutineScope.launch {
                    // For MVP: just reload dummy data
                    posts = getDummyPosts()
                }
            }) {
                Text("Refresh")
            }
        }
        // Feed List
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Trending section (bonus)
            if (isPublic && posts.any { it.isTrending }) {
                item {
                    Text(
                        "Trending in GGV",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(posts.filter { it.isTrending }) { post ->
                    FeedCard(post)
                }
            }
            // Main feed
            items(posts.filter {
                if (isPublic) true else it.isConnection
            }.sortedWith(compareByDescending<Post> { it.isConnection }.thenByDescending { it.isSameBranch })) { post ->
                FeedCard(post)
            }
        }
    }
}

@Composable
fun FeedCard(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(12.dp)) {
            // Avatar (MVP: circle with initials)
            Box(
                Modifier.size(48.dp).align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            post.userName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString(""),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(post.userName, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Text(post.userBranch, color = Color.Gray, fontSize = 13.sp)
                    if (post.gamification != null) {
                        Spacer(Modifier.width(6.dp))
                        Text(post.gamification, fontSize = 16.sp)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(post.contentText)
                // Image (MVP: skip or placeholder)
                if (post.contentImage != null) {
                    Spacer(Modifier.height(8.dp))
                    // Image(painterResource(post.contentImage), contentDescription = null, modifier = Modifier.height(120.dp).fillMaxWidth())
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { /* Like logic */ }) {
                        Text("👍")
                    }
                    Text("${post.likeCount}", fontSize = 13.sp)
                    Spacer(Modifier.width(16.dp))
                    IconButton(onClick = { /* Comment logic */ }) {
                        Text("💬")
                    }
                    Text("${post.commentCount}", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun SegmentedButton(options: List<String>, selectedIndex: Int, onSelected: (Int) -> Unit) {
    Row {
        options.forEachIndexed { idx, label ->
            Button(
                onClick = { onSelected(idx) },
                colors = if (selectedIndex == idx) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Text(label)
            }
        }
    }
}