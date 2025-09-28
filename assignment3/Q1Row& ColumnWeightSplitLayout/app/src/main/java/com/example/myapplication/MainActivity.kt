package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row // Keep this import
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeightScreenLayout(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WeightScreenLayout(modifier: Modifier = Modifier) {
    //row as the main container for both boxes (25% and 75%)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //first box takes up 25% of screen
        Box(
            modifier = Modifier
                .weight(0.25f) // 25% width
                .fillMaxHeight() //
                .background(color = Color(0xFFE91E63)) // Pink color
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "25%\nWidth",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        // second box takes 75% of the rows's width
        Box(
            modifier = Modifier
                .weight(0.75f) // 75% width
                .fillMaxHeight() //
                .background(color = Color(0xFF673AB7)) // Purple outer box
                .padding(8.dp)
        ) {
            // use row for horizontal distribution of children
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // this row will have three children weighted 2:3:5 for proportion width
                Box(
                    modifier = Modifier
                        .weight(2f) // takes 2 parts of the rows's width (2/10 = 20%)
                        .fillMaxHeight()
                        .background(Color(0xFFE6E6FA)) //lavendar color
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "20% Width",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(3f) // takes 3 parts of the rows's width
                        .fillMaxHeight() //
                        .background(Color(0xFFCBC3E3	) )// light purple
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "30% Width",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(5f) // takes 5 parts of the rows's width
                        .fillMaxHeight()
                        .background(Color(0xFFCF9FFF))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "50% Width", // Updated text
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, widthDp = 400, heightDp = 350)
@Composable
fun WeightScreenLayoutPreview() {
    MyApplicationTheme {
        WeightScreenLayout()
    }
}
