package com.example.polyline_and_polygon

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.polyline_and_polygon.ui.theme.Polyline_and_PolygonTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Polyline_and_PolygonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PolylineMap()
                }
            }
        }
    }
}

@Composable
fun PolylineMap() {
    // state variables for customization ---
    var polylineColor by remember { mutableStateOf(Color.Red) }
    var polylineWidth by remember { mutableFloatStateOf(10f) }
    var polygonFillColor by remember { mutableStateOf(Color.Blue.copy(alpha = 0.3f)) }

    // allowing users to customize the color and width of the polyline and polygon
    val colorOptions = listOf(Color.Red, Color.Blue, Color.Green, Color.Black)
    val widthOptions = listOf(5f, 10f, 20f)
    val context = LocalContext.current

    //  Golden Gate Bridge Path trail
    val goldenGateBridgePath = remember {
        listOf(
            LatLng(37.8070, -122.4750), // South end near visitor center
            LatLng(37.8199, -122.4783), // Mid-point of the bridge
            LatLng(37.8324, -122.4815)  // North end near Vista Point
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(goldenGateBridgePath[1], 14f) // Zoom to the middle of the bridge
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            //   state variables for polyline properties
            Polyline(
                points = goldenGateBridgePath,
                color = polylineColor, //use staet for colro
                width = polylineWidth, // Use state for width
                clickable = true,
                onClick = {
                    Toast.makeText(context, "You tapped the Golden Gate Bridge Path!", Toast.LENGTH_SHORT).show()
                }
            )

            //  state variables for polygon properties
            // A polygon will connect the start and end points to form a shape
            Polygon(
                points = goldenGateBridgePath,
                fillColor = polygonFillColor, //  state for color
                strokeColor = Color.Black,
                strokeWidth = 2f,
                clickable = true,
                onClick = {
                    Toast.makeText(context, "This is the general Bay Area!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Position buttons at the bottom
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                // cycle to the next color
                val currentIndex = colorOptions.indexOf(polylineColor)
                val nextIndex = (currentIndex + 1) % colorOptions.size
                polylineColor = colorOptions[nextIndex]
                // Also change polygon color for demonstration
                polygonFillColor = colorOptions[nextIndex].copy(alpha = 0.5f)
            }) {
                Text(text = "Change Color")
            }

            Button(onClick = {
                // cycle to the next width
                val currentIndex = widthOptions.indexOf(polylineWidth)
                polylineWidth = widthOptions[(currentIndex + 1) % widthOptions.size]
            }) {
                Text(text = "Change Width")
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Polyline_and_PolygonTheme {
        Greeting("Android")
    }
}
