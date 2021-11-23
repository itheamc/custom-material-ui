package com.example.fashionhub.ui.scaffold

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fashionhub.ui.nav.CustomNavigationBar
import com.example.fashionhub.ui.staggeredgrid.GridCells
import com.example.fashionhub.ui.staggeredgrid.GridDirection
import com.example.fashionhub.ui.staggeredgrid.StaggeredGrid
import kotlin.random.Random

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

    var items by rememberSaveable {
        mutableStateOf(20)
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
                                onClick = {
                                    selected = it
                                    items = it.label.length * 5
                                },
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
                                onClick = {
                                    selected = it
                                    items = it.label.length * 5
                                },
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
            Log.d("Amit", "FashionHubApp: ${it.toString()}")
            StaggeredGrid(
                modifier = Modifier
//                    .padding(it)
                    .fillMaxSize(),
                gridDirection = GridDirection.Vertical,
                cells = GridCells.Adaptive(170.dp),
//                cells = GridCells.Fixed(3),
                gap = 12.dp
            ) {
                (1 until items).forEachIndexed { _, n ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(((n * 10) + 200).dp)
                            .clickable(
                                role = Role.Button
                            ) { }
                            .background(Color(
                                alpha = 255,
                                red = Random.nextInt(256),
                                green = Random.nextInt(256),
                                blue = Random.nextInt(256),
                            )),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "This is item $n")
                    }
                }
            }
        }
    )
}