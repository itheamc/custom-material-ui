package com.example.fashionhub.ui.scaffold

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    icon: @Composable () -> Unit,
    text: @Composable() (() -> Unit)? = null,
    screenSize: ScreenSize
) {

    val config = LocalConfiguration.current
    val width = config.screenWidthDp
    val height = config.screenHeightDp
    if (text == null) {
        if (width <= 560) {
            SmallFloatingActionButton(
                modifier = modifier,
                onClick = onClick,
                interactionSource = interactionSource,
                elevation = elevation,
                content = icon
            )
        } else if (width in 561..1079) {
            FloatingActionButton(
                modifier = modifier,
                onClick = onClick,
                interactionSource = interactionSource,
                elevation = elevation,
                content = icon
            )
        } else {
            LargeFloatingActionButton(
                modifier = modifier,
                onClick = onClick,
                interactionSource = interactionSource,
                elevation = elevation,
                content = icon
            )
        }
    } else {
        CustomExtendedFloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            interactionSource = interactionSource,
            elevation = elevation,
            icon = icon,
            text = text
        )
    }

}


@Composable
fun CustomExtendedFloatingActionButton(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(16.0.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
) {
    FloatingActionButton(
        modifier = modifier.sizeIn(
            minWidth = 70.dp,
            minHeight = 48.dp,
        ),
        onClick = onClick,
        interactionSource = interactionSource,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        val startPadding = if (icon == null) {
            20.dp
        } else {
            12.dp
        }
        Row(
            modifier = Modifier.padding(
                start = startPadding,
                end = 20.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(Modifier.width(12.dp))
            }
            ProvideTextStyle(
                value = MaterialTheme.typography.labelLarge,
                content = text,
            )
        }
    }
}

