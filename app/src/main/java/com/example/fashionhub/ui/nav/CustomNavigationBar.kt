package com.example.fashionhub.ui.nav

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fashionhub.ui.scaffold.ScreenSize
import kotlin.math.roundToInt

@Composable
fun CustomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = CustomNavigationBar.tonalElevation,
    navigationBarContent: @Composable RowScope.() -> Unit,
    navigationRailContent: @Composable ColumnScope.() -> Unit,
    navigationRailHeader: @Composable (ColumnScope.() -> Unit)? = null,
    screenSize: ScreenSize
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {

        // If Device isn't Compact
        AnimatedVisibility(
            visible = screenSize != ScreenSize.Compact,
            enter = slideInHorizontally(animationSpec = tween(durationMillis = CustomNavigationBar.animDuration)) { fullWidth ->
                -fullWidth
            } + fadeIn(
                animationSpec = tween(durationMillis = CustomNavigationBar.animDuration)
            ),
            exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessLow)) {
                -CustomNavigationBar.NavigationRailWidth
            } + fadeOut()
        ) {
           NavigationRail(
               modifier = modifier,
               containerColor = containerColor,
               contentColor = contentColor,
               header = navigationRailHeader,
               content = navigationRailContent
           )
        }


        // If Device is compact
        AnimatedVisibility(
            visible = screenSize == ScreenSize.Compact,
            enter = slideInVertically(
                initialOffsetY = { CustomNavigationBar.NavigationBarHeight },
                animationSpec = tween(durationMillis = CustomNavigationBar.animDuration)
            ) + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(
                initialAlpha = 0.3f,
                animationSpec = tween(durationMillis = CustomNavigationBar.animDuration)
            ),
            exit = slideOutVertically(
                targetOffsetY = { CustomNavigationBar.NavigationBarHeight }
            ) + shrinkVertically() + fadeOut()
        ) {
            NavigationBar(
                modifier = modifier,
                containerColor = containerColor,
                tonalElevation = tonalElevation,
                content = navigationBarContent
            )
        }
    }
}


/**
 * --------------------------------------------
 * CustomNavigationBarItem
 */
@Composable
fun ColumnScope.CustomNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable() (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: NavigationRailItemColors = NavigationRailItemDefaults.colors()
) {
    val styledIcon = @Composable {
        val iconColor by colors.iconColor(selected = selected)
        CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
    }

    val styledLabel: @Composable (() -> Unit)? = label?.let {
        @Composable {
            val style = MaterialTheme.typography.labelMedium
            val textColor by colors.textColor(selected = selected)
            CompositionLocalProvider(LocalContentColor provides textColor) {
                ProvideTextStyle(style, content = label)
            }
        }
    }

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = rememberRipple(),
            )
            .size(
                width = CustomNavigationBar.NavigationRailWidth.dp,
                height = CustomNavigationBar.NoLabelActiveIndicatorHeight
            ),
        contentAlignment = Alignment.Center
    ) {
        ItemBaseLineLayoutContainer(
            selected = selected,
            styledIcon = styledIcon,
            styledLabel = styledLabel,
            alwaysShowLabel = alwaysShowLabel,
            colors = colors
        )
    }

}

/**
 * --------------------------------------------
 * CustomNavigationBarItem
 */
@Composable
fun RowScope.CustomNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: NavigationRailItemColors = NavigationRailItemDefaults.colors(),
) {
    val styledIcon = @Composable {
        val iconColor by colors.iconColor(selected = selected)
        CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
    }

    val styledLabel: @Composable (() -> Unit)? = label?.let {
        @Composable {
            val style = MaterialTheme.typography.labelMedium
            val textColor by colors.textColor(selected = selected)
            CompositionLocalProvider(LocalContentColor provides textColor) {
                ProvideTextStyle(style, content = label)
            }
        }
    }

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = rememberRipple(),
            )
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        ItemBaseLineLayoutContainer(
            selected = selected,
            styledIcon = styledIcon,
            styledLabel = styledLabel,
            alwaysShowLabel = alwaysShowLabel,
            colors = colors
        )
    }

}


/**
 * BoxScope Composable
 *
 */
@Composable
private fun BoxScope.ItemBaseLineLayoutContainer(
    selected: Boolean,
    styledIcon: @Composable () -> Unit,
    styledLabel: @Composable() (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    colors: NavigationRailItemColors = NavigationRailItemDefaults.colors(),
) {
    val animationProgress: Float by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(150)
    )

    val indicator = @Composable {
        Box(
            Modifier
                .layoutId(CustomNavigationBar.IndicatorLayoutIdTag)
                .background(
                    color = colors.indicatorColor.copy(alpha = animationProgress),
                    shape = if (styledLabel != null) {
                        CustomNavigationBar.ActiveIndicatorShape
                    } else {
                        CustomNavigationBar.NoLabelActiveIndicatorShape
                    }
                )
        )
    }

    CustomNavigationBarItemBaselineLayout(
        indicator = indicator,
        icon = styledIcon,
        label = styledLabel,
        alwaysShowLabel = alwaysShowLabel,
        animationProgress = animationProgress,
    )
}


/**
 * Base layout for a [CustomNavigationBarItem]
 */

@Composable
private fun CustomNavigationBarItemBaselineLayout(
    indicator: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)?,
    alwaysShowLabel: Boolean,
    animationProgress: Float,
) {
    Layout({
        if (animationProgress > 0) {
            indicator()
        }

        Box(Modifier.layoutId(CustomNavigationBar.IconLayoutIdTag)) { icon() }

        if (label != null) {
            Box(
                Modifier
                    .layoutId(CustomNavigationBar.LabelLayoutIdTag)
                    .alpha(if (alwaysShowLabel) 1f else animationProgress)
            ) { label() }
        }
    }) { measurables, constraints ->
        val iconPlaceable =
            measurables.first { it.layoutId == CustomNavigationBar.IconLayoutIdTag }
                .measure(constraints)

        val totalIndicatorWidth =
            iconPlaceable.width + (CustomNavigationBar.IndicatorHorizontalPadding * 2).roundToPx()
        val animatedIndicatorWidth = (totalIndicatorWidth * animationProgress).roundToInt()
        val indicatorVerticalPadding = if (label == null) {
            CustomNavigationBar.IndicatorVerticalPaddingNoLabel
        } else {
            CustomNavigationBar.IndicatorVerticalPaddingWithLabel
        }
        val indicatorHeight = iconPlaceable.height + (indicatorVerticalPadding * 2).roundToPx()

        val indicatorPlaceable =
            measurables
                .firstOrNull { it.layoutId == CustomNavigationBar.IndicatorLayoutIdTag }
                ?.measure(
                    Constraints.fixed(
                        width = animatedIndicatorWidth,
                        height = indicatorHeight
                    )
                )

        val labelPlaceable =
            label?.let {
                measurables
                    .first { it.layoutId == CustomNavigationBar.LabelLayoutIdTag }
                    .measure(
                        // Measure with loose constraints for height as we don't want the label to
                        // take up more space than it needs
                        constraints.copy(minHeight = 0)
                    )
            }

        if (label == null) {
            placeIcon(iconPlaceable, indicatorPlaceable, constraints)
        } else {
            placeLabelAndIcon(
                labelPlaceable!!,
                iconPlaceable,
                indicatorPlaceable,
                constraints,
                alwaysShowLabel,
                animationProgress,
            )
        }
    }
}

/**
 * Places the provided [iconPlaceable], and possibly [indicatorPlaceable] if it exists, in the
 * center of the provided [constraints].
 */
private fun MeasureScope.placeIcon(
    iconPlaceable: Placeable,
    indicatorPlaceable: Placeable?,
    constraints: Constraints,
): MeasureResult {
    val width = constraints.maxWidth
    val height = constraints.maxHeight

    val iconX = (width - iconPlaceable.width) / 2
    val iconY = (height - iconPlaceable.height) / 2

    return layout(width, height) {
        indicatorPlaceable?.let {
            val indicatorX = (width - it.width) / 2
            val indicatorY = (height - it.height) / 2
            it.placeRelative(indicatorX, indicatorY)
        }
        iconPlaceable.placeRelative(iconX, iconY)
    }
}

/**
 * Places the provided [labelPlaceable], [iconPlaceable], and [indicatorPlaceable] in the correct
 * position, depending on [alwaysShowLabel] and [animationProgress].
 */
private fun MeasureScope.placeLabelAndIcon(
    labelPlaceable: Placeable,
    iconPlaceable: Placeable,
    indicatorPlaceable: Placeable?,
    constraints: Constraints,
    alwaysShowLabel: Boolean,
    animationProgress: Float,
): MeasureResult {
    val height = constraints.maxHeight

    // Label should be `ItemVerticalPadding` from the bottom
    val labelY =
        height - labelPlaceable.height - CustomNavigationBar.NavigationRailVerticalPadding.roundToPx()

    // Icon (when selected) should be `ItemVerticalPadding` from the top
    val selectedIconY = CustomNavigationBar.NavigationRailVerticalPadding.roundToPx()
    val unselectedIconY =
        if (alwaysShowLabel) selectedIconY else (height - iconPlaceable.height) / 2

    // How far the icon needs to move between unselected and selected states
    val iconDistance = unselectedIconY - selectedIconY

    // The interpolated fraction of iconDistance that all placeables need to move based on
    // animationProgress, since the icon is higher in the selected state.
    val offset = (iconDistance * (1 - animationProgress)).roundToInt()

    val width = constraints.maxWidth
    val labelX = (width - labelPlaceable.width) / 2
    val iconX = (width - iconPlaceable.width) / 2

    return layout(width, height) {
        indicatorPlaceable?.let {
            val indicatorX = (width - it.width) / 2
            val indicatorY =
                selectedIconY - CustomNavigationBar.IndicatorVerticalPaddingWithLabel.roundToPx()
            it.placeRelative(indicatorX, indicatorY + offset)
        }
        if (alwaysShowLabel || animationProgress != 0f) {
            labelPlaceable.placeRelative(labelX, labelY + offset)
        }
        iconPlaceable.placeRelative(iconX, selectedIconY + offset)
    }
}


/**
 * --------------------------------------------
 * Properties for custom navbar
 */
private object CustomNavigationBar {
    val tonalElevation: Dp = 3.dp
    const val animDuration: Int = 1000
    const val NavigationRailWidth: Int = 80
    val NavigationRailVerticalPadding = 4.dp
    val NavigationRailSpacedBy = 4.dp
    val NavigationRailSpacerHeight = 8.dp
    const val NavigationBarHeight: Int = 80
    val NoLabelActiveIndicatorHeight = 56.0.dp
    const val IndicatorLayoutIdTag: String = "indicator"
    const val IconLayoutIdTag: String = "icon"
    const val LabelLayoutIdTag: String = "label"
    val ActiveIndicatorShape = RoundedCornerShape(16.dp)
    val NoLabelActiveIndicatorShape = RoundedCornerShape(28.dp)
    val ActiveIndicatorWidth = 56.dp
    val ActiveIndicatorHeight = 32.dp
    val IconSize = 24.dp
    val IndicatorHorizontalPadding = (ActiveIndicatorWidth - IconSize) / 2
    val IndicatorVerticalPaddingWithLabel: Dp = (ActiveIndicatorHeight - IconSize) / 2
    val IndicatorVerticalPaddingNoLabel: Dp = (NoLabelActiveIndicatorHeight - IconSize) / 2
}