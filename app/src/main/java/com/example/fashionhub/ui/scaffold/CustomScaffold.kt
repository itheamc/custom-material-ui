package com.example.fashionhub.ui.scaffold

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp


@ExperimentalMaterial3Api
@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: CustomScaffoldState = rememberCustomScaffoldState(),
    topBar: @Composable () -> Unit = {},
    navigationBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    drawerContent: @Composable() (ColumnScope.() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit,
    screenSize: ScreenSize
) {

    val child = @Composable { childModifier: Modifier ->
        Surface(modifier = childModifier, color = containerColor, contentColor = contentColor) {
            CustomScaffoldLayout2(
                fabPosition = floatingActionButtonPosition,
                topBar = topBar,
                navigationBar = navigationBar,
                content = content,
                fab = floatingActionButton,
                screenSize = screenSize
            )
        }
    }

    if (screenSize == ScreenSize.Compact && drawerContent != null) {
        NavigationDrawer(
            modifier = modifier,
            drawerState = scaffoldState.drawerState,
            drawerContent = drawerContent,
            content = { child(modifier) }
        )
    } else {
        child(modifier)
    }
}


@ExperimentalMaterial3Api
@Composable
private fun CustomScaffoldLayout(
    fabPosition: FabPosition,
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    fab: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    navigationRail: @Composable () -> Unit,
    screenSize: ScreenSize
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val topBarPlaceable = subcompose(ScaffoldLayoutContent.TopBar, topBar).mapNotNull {
                it.measure(looseConstraints).takeIf { screenSize == ScreenSize.Compact }
            }

            val topBarHeight = topBarPlaceable.maxByOrNull { it.height }?.height ?: 0

            val fabPlaceable =
                subcompose(ScaffoldLayoutContent.Fab, fab).mapNotNull { measurable ->
                    measurable.measure(looseConstraints)
                        .takeIf { it.height != 0 && it.width != 0 }
                }

            val fabPlacement = if (fabPlaceable.isNotEmpty()) {
                val fabWidth = fabPlaceable.maxByOrNull { it.width }!!.width
                val fabHeight = fabPlaceable.maxByOrNull { it.height }!!.height
                // FAB distance from the left of the layout, taking into account LTR / RTL
                val fabLeftOffset = if (fabPosition == FabPosition.End) {
                    if (layoutDirection == LayoutDirection.Ltr) {
                        layoutWidth - FabSpacing.roundToPx() - fabWidth
                    } else {
                        FabSpacing.roundToPx()
                    }
                } else {
                    (layoutWidth - fabWidth) / 2
                }

                FabPlacement(
                    left = fabLeftOffset,
                    width = fabWidth,
                    height = fabHeight
                )
            } else {
                null
            }

            /*
            --------------------------
            Navigation Rail Composable
             */
            val navRailPlaceable =
                subcompose(
                    ScaffoldLayoutContent.NavigationRail,
                    navigationRail
                ).mapNotNull { measurable ->
                    measurable.measure(looseConstraints)
                        .takeIf { screenSize != ScreenSize.Compact }
                }

            val navRailWidth = navRailPlaceable.minByOrNull { it.width }?.width ?: 0

            val bottomBarPlaceable = subcompose(ScaffoldLayoutContent.BottomBar) {
                CompositionLocalProvider(
                    LocalFabPlacement provides fabPlacement,
                    content = bottomBar
                )
            }.mapNotNull {
                it.measure(looseConstraints).takeIf { screenSize == ScreenSize.Compact }
            }

            val bottomBarHeight = bottomBarPlaceable.maxByOrNull { it.height }?.height ?: 0
            val fabOffsetFromBottom = fabPlacement?.let {
                if (bottomBarHeight == 0) {
                    it.height + FabSpacing.roundToPx()
                } else {
                    // Total height is the bottom bar height + the FAB height + the padding
                    // between the FAB and bottom bar
                    bottomBarHeight + it.height + FabSpacing.roundToPx()
                }
            }

            val bodyContentHeight = layoutHeight - topBarHeight - bottomBarHeight
            val bodyContentWidth = layoutWidth - navRailWidth

            val bodyContentPlaceable = subcompose(ScaffoldLayoutContent.MainContent) {
                val innerPadding = PaddingValues(all = 12.dp)
                content(innerPadding)
            }.map {
                it.measure(
                    looseConstraints.copy(
                        maxHeight = bodyContentHeight,
                        maxWidth = bodyContentWidth
                    )
                )
            }

            // Placing to control drawing order to match default elevation of each placeable

            bodyContentPlaceable.forEach {
                it.place(navRailWidth, topBarHeight)
            }
            topBarPlaceable.forEach {
                it.place(0, 0)
            }

            // NavigationRail Placement
            navRailPlaceable.forEach {
                it.place(0, 0)
            }

            // The bottom bar is always at the bottom of the layout
            bottomBarPlaceable.forEach {
                it.place(0, layoutHeight - bottomBarHeight)
            }

            // Explicitly not using placeRelative here as `leftOffset` already accounts for RTL
            fabPlacement?.let { placement ->
                fabPlaceable.forEach {
                    it.place(placement.left, layoutHeight - fabOffsetFromBottom!!)
                }
            }
        }
    }
}

/**
 * ------------------------------------------------------------------------------
 * -----------------------------------------------------------------------------
 */

@ExperimentalMaterial3Api
@Composable
private fun CustomScaffoldLayout2(
    fabPosition: FabPosition,
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    fab: @Composable () -> Unit,
    navigationBar: @Composable () -> Unit,
    screenSize: ScreenSize
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        /*
        -------------------------------------------------------------------------------
        Layout Design and Content Placement Starts from here
         */
        layout(layoutWidth, layoutHeight) {
            // For TopBar
            val topBarPlaceables = subcompose(ScaffoldLayoutContent.TopBar, topBar).mapNotNull {
                it.measure(looseConstraints).takeIf { screenSize == ScreenSize.Compact }
            }

            val topBarHeight = topBarPlaceables.maxByOrNull { it.height }?.height ?: 0


            // For Floating Action Button
            val fabPlaceables =
                subcompose(ScaffoldLayoutContent.Fab, fab).mapNotNull { measurable ->
                    measurable.measure(looseConstraints)
                        .takeIf { it.height != 0 && it.width != 0 }
                }

            val fabPlacement = if (fabPlaceables.isNotEmpty()) {
                val fabWidth = fabPlaceables.maxByOrNull { it.width }!!.width
                val fabHeight = fabPlaceables.maxByOrNull { it.height }!!.height
                // FAB distance from the left of the layout, taking into account LTR / RTL
                val fabLeftOffset = if (fabPosition == FabPosition.End) {
                    if (layoutDirection == LayoutDirection.Ltr) {
                        layoutWidth - FabSpacing.roundToPx() - fabWidth
                    } else {
                        FabSpacing.roundToPx()
                    }
                } else {
                    (layoutWidth - fabWidth) / 2
                }

                FabPlacement(
                    left = fabLeftOffset,
                    width = fabWidth,
                    height = fabHeight
                )
            } else {
                null
            }

            /*
            --------------------------
            Navigation Rail Composable
             */
            val navRailPlaceables =
                subcompose(
                    slotId = ScaffoldLayoutContent.NavigationRail,
                    content = navigationBar
                ).mapNotNull { measurable ->
                    measurable.measure(looseConstraints)
                        .takeIf { screenSize != ScreenSize.Compact }
                }

            val navigationRailWidth = navRailPlaceables.maxByOrNull { it.width }?.width ?: 0


            // For BottomBar Placement
            val bottomBarPlaceable = subcompose(slotId = ScaffoldLayoutContent.BottomBar) {
                CompositionLocalProvider(
                    LocalFabPlacement provides fabPlacement,
                    content = navigationBar
                )
            }.mapNotNull {
                it.measure(looseConstraints).takeIf { screenSize == ScreenSize.Compact }
            }

            val bottomBarHeight = bottomBarPlaceable.maxByOrNull { it.height }?.height ?: 0
            val fabOffsetFromBottom = fabPlacement?.let {
                if (bottomBarHeight == 0) {
                    it.height + FabSpacing.roundToPx()
                } else {
                    // Total height is the bottom bar height + the FAB height + the padding
                    // between the FAB and bottom bar
                    bottomBarHeight + it.height + FabSpacing.roundToPx()
                }
            }

            val bodyContentHeight = layoutHeight - topBarHeight - bottomBarHeight
            val bodyContentWidth = layoutWidth - navigationRailWidth

            val bodyContentPlaceable = subcompose(ScaffoldLayoutContent.MainContent) {
                val innerPadding = PaddingValues(all = 12.dp)
                content(innerPadding)
            }.map {
                it.measure(
                    looseConstraints.copy(
                        maxHeight = bodyContentHeight,
                        maxWidth = bodyContentWidth
                    )
                )
            }

            // Placing to control drawing order to match default elevation of each placeable

            bodyContentPlaceable.forEach {
                it.place(navigationRailWidth, topBarHeight)
            }
            topBarPlaceables.forEach {
                it.place(0, 0)
            }

            // NavigationRail Placement
            navRailPlaceables.forEach {
                it.place(0, 0)
            }

            // The bottom bar is always at the bottom of the layout
            bottomBarPlaceable.forEach {
                it.place(0, layoutHeight - bottomBarHeight)
            }

            // Explicitly not using placeRelative here as `leftOffset` already accounts for RTL
            fabPlacement?.let { placement ->
                fabPlaceables.forEach {
                    it.place(placement.left, layoutHeight - fabOffsetFromBottom!!)
                }
            }
        }
    }
}


/**
 * ------------------------------------------------------------------------------
 * ------------------------------------------------------------------------------
 */

/**
 * State for [Scaffold] composable component.
 * @param drawerState the drawer state
 */
@Stable
@ExperimentalMaterial3Api
class CustomScaffoldState(
    val drawerState: DrawerState,
)

/**
 * Creates a [ScaffoldState] with the default animation clock and memorizes it.
 * @param drawerState the drawer state
 */
@ExperimentalMaterial3Api
@Composable
fun rememberCustomScaffoldState(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
): CustomScaffoldState = remember {
    CustomScaffoldState(drawerState)
}

/**
 * The possible positions for a [FloatingActionButton] attached to a [Scaffold].
 */
@ExperimentalMaterial3Api
@Suppress("INLINE_CLASS_DEPRECATED", "EXPERIMENTAL_FEATURE_WARNING")
inline class FabPosition internal constructor(@Suppress("unused") private val value: Int) {
    companion object {
        val Center = FabPosition(0)
        val End = FabPosition(1)
    }

    override fun toString(): String {
        return when (this) {
            Center -> "FabPosition.Center"
            else -> "FabPosition.End"
        }
    }
}

/**
 * Placement information for a [FloatingActionButton] inside a [Scaffold].
 *
 * @property left the FAB's offset from the left edge of the bottom bar, already adjusted for RTL
 * support
 * @property width the width of the FAB
 * @property height the height of the FAB
 */
@Immutable
internal class FabPlacement(
    val left: Int,
    val width: Int,
    val height: Int
)

/**
 * CompositionLocal containing a [FabPlacement] that is used to calculate the FAB bottom offset.
 */
internal val LocalFabPlacement = staticCompositionLocalOf<FabPlacement?> { null }

// FAB spacing above the bottom bar / bottom of the Scaffold
private val FabSpacing = 16.dp

private enum class ScaffoldLayoutContent { TopBar, MainContent, Fab, BottomBar, NavigationRail, NavigationBar }
