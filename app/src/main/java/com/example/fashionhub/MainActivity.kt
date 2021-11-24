package com.example.fashionhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.fashionhub.ui.charts.CustomCharts
import com.example.fashionhub.ui.scaffold.FashionHubApp
import com.example.fashionhub.ui.scaffold.rememberScreenSize
import com.example.fashionhub.ui.testing.TestingVerticalSlideInAnim
import com.example.fashionhub.ui.theme.FashionHubTheme

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val screenSize = rememberScreenSize()
            FashionHubTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//                }
//                FashionHubApp(screenSize)
//                TestingVerticalSlideInAnim()
                Column {
                    CustomCharts(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ProductScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Gradient(
            colors = listOf(Color(0xFF806399), Color(0xFF689BD4)),
        )

        Box(
            modifier = Modifier
                .size(width = 420.dp, height = 150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0x11FFFFFF))
                .padding(horizontal = 16.dp),
            content = {
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { },
                    painter = rememberImagePainter(
                        data = "https://unsplash.com/photos/uR51HXLO7G0/download?ixid=MnwxMjA3fDB8MXxzZWFyY2h8Mzl8fGZhc2hpb258ZW58MHx8fHwxNjM3MDQ2MDc0&force=true&w=640",
                        builder = {
                            placeholder(R.drawable.ic_launcher_foreground)
                            transformations(CircleCropTransformation())
                            crossfade(true)
                            crossfade(1000)
                        }),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            },
            contentAlignment = Alignment.CenterStart
        )

    }
}

@Composable
fun Gradient(
    colors: List<Color>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = colors
                )
            )
    )
}


/**
 * --------------------------------------
 * Drawer
 */
@Composable
fun Drawer() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.30f)
                .background(color = Color.Red),
            content = {

            }
        )

        // Body Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = Color.Cyan),
            content = {

            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FashionHubTheme {

    }
}