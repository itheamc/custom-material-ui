package com.example.fashionhub.ui.testing

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun TestingHorizontalSlideInAnim() {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(animationSpec = tween(durationMillis = 2000)) { fullWidth ->
                -fullWidth
            } + fadeIn(
                animationSpec = tween(durationMillis = 2000)
            ),
            exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessLow)) {
                -80
            } + fadeOut()
        ) {
            // Content that needs to appear/disappear goes here:
            Box(
                Modifier
                    .width(80.dp)
                    .requiredHeight(200.dp)
                    .background(Color.Red)
            ) {
                Text(text = "Box")
            }
        }

        IconButton(modifier = Modifier.align(Alignment.End), onClick = { visible = !visible }) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun TestingVerticalSlideInAnim() {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(modifier = Modifier.align(Alignment.End), onClick = { visible = !visible }) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { 200 }
            ) + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically(
                targetOffsetY = { 200 }
            ) + shrinkVertically() + fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .requiredHeight(200.dp)
                    .background(Color.Red)
            )
        }
    }


}

