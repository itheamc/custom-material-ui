package com.example.fashionhub.ui.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.navigation.NavBackStackEntry

enum class Screens(
    val label: String,
    val icon: ImageVector,
    val topBar: @Composable (label: String) -> Unit = {},
    val type: ScreenTypes = ScreenTypes.Secondary
) {
    HomeScreen(
        label = "Home",
        icon = Icons.Filled.Home,
        topBar = @Composable {
            SmallTopAppBar(
                title = {
                    Text(text = it)
                }
            )
        },
        type = ScreenTypes.Primary
    ),
    CartScreen(
        label = "Cart",
        icon = Icons.Filled.ShoppingCart,
        topBar = @Composable {
            SmallTopAppBar(
                title = {
                    Text(text = "Cart")
                }
            )
        },
        type = ScreenTypes.Primary
    ),
    NotificationScreen(
        label = "Notification",
        icon = Icons.Filled.Notifications,
        topBar = @Composable {
            SmallTopAppBar(
                title = {
                    Text(text = "Notifications")
                }
            )
        },
        type = ScreenTypes.Primary
    ),
    SettingsScreen(
        label = "Settings",
        icon = Icons.Filled.Settings,
        topBar = @Composable {
            SmallTopAppBar(
                title = {
                    Text(text = "Settings")
                }
            )
        },
        type = ScreenTypes.Primary
    ),
    ProfileScreen(
        label = "Profile",
        icon = Icons.Filled.Settings,
//        topBar = @Composable {
//            SmallTopAppBar(
//                title = {
//                    Text(text = "Profile")
//                }
//            )
//        }
    );

    companion object {
        fun fromBackStackEntry(backStackEntry: State<NavBackStackEntry?>) =
            when (backStackEntry.value?.destination?.route?.substringBefore("/")
                ?.substringBefore("?")) {
                HomeScreen.name -> HomeScreen
                CartScreen.name -> CartScreen
                NotificationScreen.name -> NotificationScreen
                SettingsScreen.name -> SettingsScreen
                ProfileScreen.name -> ProfileScreen
                else -> HomeScreen
            }

        fun isPrimary(backStackEntry: State<NavBackStackEntry?>) =
            when (backStackEntry.value?.destination?.route?.substringBefore("/")
                ?.substringBefore("?")) {
                HomeScreen.name -> true
                CartScreen.name -> true
                NotificationScreen.name -> true
                SettingsScreen.name -> true
                else -> false
            }
    }
}


/**
 * -------------------------------------
 * Enum Class for Screen type
 */
enum class ScreenTypes {
    Primary,
    Secondary
}