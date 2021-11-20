package com.example.fashionhub.ui.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fashionhub.ui.nav.CustomNavigationBar

@ExperimentalMaterial3Api
@Composable
fun FashionHubApp(screenSize: ScreenSize) {

    val scaffoldState = rememberCustomScaffoldState()
    val screens = Screens.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = Screens.fromBackStackEntry(backstackEntry)
    var selected by rememberSaveable {
        mutableStateOf(Screens.HomeScreen)
    }

    CustomScaffold(
        scaffoldState = scaffoldState,
        screenSize = screenSize,
        topBar = {
            currentScreen.topBar
        },
        navigationBar = {
            CustomNavigationBar(
                navigationBarContent = {
                    screens.forEach {
                        if (it.type == ScreenTypes.Primary) {
                            NavigationBarItem(
                                selected = it == selected,
                                onClick = { selected = it },
                                icon = {
                                    if (it == Screens.NotificationScreen) {
                                        BadgedBox(
                                            badge = {
                                                Badge(
                                                    content = {
                                                        Text(text = "10")
                                                    }
                                                )
                                            },
                                            content = {
                                                Icon(
                                                    imageVector = it.icon,
                                                    contentDescription = it.label
                                                )
                                            }
                                        )
                                    } else {
                                        Icon(
                                            imageVector = it.icon,
                                            contentDescription = it.label
                                        )
                                    }
                                },
                                label = {
                                    Text(text = it.label)
                                },
                                alwaysShowLabel = false
                            )
                        }
                    }
                },
                navigationRailContent = {
                    screens.forEach {
                        if (it.type == ScreenTypes.Primary) {
                            NavigationRailItem(
                                selected = it == selected,
                                onClick = { selected = it },
                                icon = {
                                    if (it == Screens.NotificationScreen) {
                                        BadgedBox(
                                            badge = {
                                                Badge(
                                                    content = {
                                                        Text(text = "10")
                                                    }
                                                )
                                            },
                                            content = {
                                                Icon(
                                                    imageVector = it.icon,
                                                    contentDescription = it.label
                                                )
                                            }
                                        )
                                    } else {
                                        Icon(
                                            imageVector = it.icon,
                                            contentDescription = it.label
                                        )
                                    }
                                },
                                label = {
                                    Text(text = it.label)
                                },
                                alwaysShowLabel = false
                            )
                        }
                    }
                },
                screenSize = screenSize
            )
        },
        floatingActionButton = {
            ResponsiveFloatingActionButton(
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Create"
                    )
                },
                text = {
                    Text(text = "Create")
                },
                screenSize = screenSize
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center,
                content = { Text(text = "You are on ${selected.label} screen!!") }
            )
        }
    )
}