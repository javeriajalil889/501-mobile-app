package com.example.q2boxoverlaywithbadge

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.q2boxoverlaywithbadge.ui.theme.Q2BoxOverlayWithBadgeTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf // Added for creating mutable state
import androidx.compose.runtime.remember // Added for remembering state across recompositions
import androidx.compose.runtime.setValue // Added for state delegation

private const val TAG = "ProfileCardDemo"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Q2BoxOverlayWithBadgeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileCardScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()

                    )

                }
            }
        }
    }
}
@Composable
fun ProfileCardScreen(modifier: Modifier = Modifier) {
    Log.d(TAG, "Q2 Box Overlay with Badge :Profile Card Screen")
    var showBadge by remember { mutableStateOf(true) }
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Q2 Box Overlay with Badge :Profile Card Screen",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ProfileCard(showBadge=showBadge) // Display the profile card

        Spacer(modifier = Modifier.height(24.dp))

        // this add the button to show or hide the badge
        Button(onClick = { showBadge = !showBadge }) {
            Text(text = if (showBadge) "Hide Badge" else "Show Badge")
        }

    }
}

@Composable
fun ProfileCard(showBadge: Boolean) {
    Log.d(TAG, "ProfileCard: Composing, showBadge= $showBadge")
    Box(
        modifier = Modifier
            .size(width = 300.dp, height = 200.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFF81D4FA))
        ) {
        }
        Log.d(TAG, "ProfileCard: Composed background layer (matchParentSize)")
        Text(

            text = "Senior @ Boston University " + "\n" + "Kotlin Mobile App Developer",

            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(70.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.profile_pic), //profile pic
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(), // makes the image fill the circle
                contentScale = ContentScale.Crop //crop image to fit ciricle
            )

        }
        Log.d(TAG, "ProfileCard: Composed profile image placeholder")

        Text(
            text = "Javeria Jalil",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .background(Color.Black.copy(alpha = 0.3f), MaterialTheme.shapes.small)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Log.d(TAG, "ProfileCard: Composed user name text")
        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Log.d(TAG, "ProfileCard: Composed notification badge")

    }
}
@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        ProfileCardScreen()
    }
}