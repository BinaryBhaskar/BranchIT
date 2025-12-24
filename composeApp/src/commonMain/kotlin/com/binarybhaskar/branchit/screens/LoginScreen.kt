package com.binarybhaskar.branchit.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import branchit.composeapp.generated.resources.Res
import branchit.composeapp.generated.resources.ic_google_logo_48
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalUriHandler
import branchit.composeapp.generated.resources.branchit_black

@Composable
fun GoogleSignInButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val gradientColors = listOf(
        Color(0xFFEA4335), // Google Red
        Color(0xFF4285F4), // Google Blue
        Color(0xFFFBBC05), // Google Yellow
        Color(0xFF34A853)  // Google Green
    )
    Box(
        modifier = Modifier
            .width(240.dp)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(gradientColors),
                shape = RoundedCornerShape(32.dp),
            )
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, contentColor = Color.Black
            ),
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(Res.drawable.ic_google_logo_48), "Google Logo", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isLoading) "Signing in..." else "Login with Google",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    isLoading: Boolean,
    onGoogleLogin: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(Res.drawable.branchit_black), "Google Logo", modifier = Modifier.height(48.dp))
        Spacer(modifier = Modifier.size(18.dp))
        Text(
            text = "Unlock Your\nCommunity",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W900,
            fontSize = 38.sp,
            lineHeight = 42.sp,
            color = Color(0xFF2F2F2F),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(18.dp))
        Text(
            text = "Explore, Connect, Grow.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.size(18.dp))
        Box(modifier = Modifier, contentAlignment = Alignment.Center) {
            GoogleSignInButton(isLoading, onClick = onGoogleLogin)
        }
        Spacer(modifier = Modifier.size(18.dp))
        Text(
            buildAnnotatedString {
                append("By signing in, you agree to our ")
                pushStringAnnotation(tag = "TOS", annotation = "https://github.com/binarybhaskar")
                withStyle(SpanStyle(color = Color(0xFF4285F4))) { append("Terms of Service") }
                pop()
                append(" and ")
                pushStringAnnotation(tag = "PRIVACY", annotation = "https://github.com/binarybhaskar/")
                withStyle(SpanStyle(color = Color(0xFF4285F4))) { append("Privacy Policy") }
                pop()
                append(".")
            },
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF888888),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clickable {
                    uriHandler.openUri("https://bhaskarpatel.me")
                }
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            "This app is not officially affiliated with GGV.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp,
            color = Color(0xFF888888),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        LoginScreen(isLoading = false, onGoogleLogin = {})
    }
}