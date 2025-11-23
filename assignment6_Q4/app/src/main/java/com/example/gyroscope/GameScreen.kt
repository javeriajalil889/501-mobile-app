package com.example.gyroscope

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameScreen(rotation: FloatArray) { //pass in rotation data
    //games initial state
    var ballPosition by remember { mutableStateOf(Offset(500f, 800f)) }
    val ballRadius = 50f

    LaunchedEffect(rotation) {
        val dx = rotation[1] * -20f // tilts left/right
        val dy = rotation[0] * 20f   // tilts forward/backward

        ballPosition = Offset(
            x = ballPosition.x + dx,
            y = ballPosition.y + dy
        )
    }

    // using canvas for game elements
    Canvas(modifier = Modifier.fillMaxSize()) {
        // boundary checks to keep the ball on screen
        val canvasWidth = size.width
        val canvasHeight = size.height

        ballPosition = Offset(
            x = ballPosition.x.coerceIn(ballRadius, canvasWidth - ballRadius),
            y = ballPosition.y.coerceIn(ballRadius, canvasHeight - ballRadius)
        )

        // draw the ball
        drawCircle(
            color = Color.Green,
            radius = ballRadius,
            center = ballPosition
        )

        // draws maze wall
        // wall 1
        drawRect(
            color = Color.Black,
            topLeft = Offset(x = 100f, y = 400f),
            size = Size(width = 800f, height = 50f)
        )

        //wall 2
        drawRect(
            color = Color.Black,
            topLeft = Offset(x = 100f, y = 1200f),
            size = Size(width = 800f, height = 50f)
        )
    }
}

// Default values for preview
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen(rotation = floatArrayOf(0f, 0f, 0f))
}
