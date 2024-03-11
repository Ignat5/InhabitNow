package com.example.inhabitnow.android.navigation.main

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.all_scheduled_tasks.allScheduledTasks
import com.example.inhabitnow.android.navigation.view_all_habits.viewAllHabits
import com.example.inhabitnow.android.navigation.view_all_tasks.viewAllTasks

fun NavGraphBuilder.mainGraph() {
    composable(route = AppNavDest.MainGraphDestination.route) {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        Scaffold(
            topBar = {
                val titleText = remember(currentBackStackEntry?.destination?.route) {
                    val mainNavDest =
                        (findMainNavDestByRoute(currentBackStackEntry?.destination?.route)
                            ?: MainNavDest.AllScheduledTasksDestination)
                    mainNavDest.toTitleText()
                }
                ScreenAppBar(
                    titleText = titleText,
                    onMenuClick = { /*TODO*/ },
                    onSearchClick = { /* TODO */ }
                )
            },
            bottomBar = {
                ScreenBottomBar(
                    currentBackStackEntry = currentBackStackEntry,
                    onNavDestClick = { mainNavDest ->
                        if (currentBackStackEntry?.destination?.route != mainNavDest.route) {
                            navController.navigate(
                                route = mainNavDest.route,
                                navOptions = navOptions {
                                    this.launchSingleTop = true
                                    this.popUpTo(AppNavDest.MainGraphDestination.route) {
                                        this.inclusive = false
                                    }
                                }
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ScreenFAB(onClick = { /* TODO */ })
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            NavHost(
                navController = navController,
                startDestination = MainNavDest.AllScheduledTasksDestination.route,
                route = AppNavDest.MainGraphDestination.route,
                modifier = Modifier.consumeWindowInsets(it)
            ) {
                allScheduledTasks()
                viewAllHabits()
                viewAllTasks()
            }
        }
    }
}

@Composable
private fun ScreenBottomBar(
    currentBackStackEntry: NavBackStackEntry?,
    onNavDestClick: (MainNavDest) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val allDestinations = remember { MainNavDest.allDestinations }
        allDestinations.forEach { mainNavDest ->
            val isSelected = remember(currentBackStackEntry?.destination?.route) {
                currentBackStackEntry?.destination?.hierarchy?.any { it.route == mainNavDest.route } ?: false
            }
            NavigationBarItem(
                selected = isSelected,
                label = {
                    val titleText = remember {
                        mainNavDest.toTitleText()
                    }
                    Text(
                        text = titleText,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                onClick = {
                    onNavDestClick(mainNavDest)
                },
                icon = {
                    val iconId = remember {
                        mainNavDest.toIconResId()
                    }
                    Icon(
                        painter = painterResource(iconId),
                        tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenAppBar(
    titleText: String,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun ScreenFAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null
        )
    }
}

private fun MainNavDest.toTitleText() = when (this) {
    is MainNavDest.AllScheduledTasksDestination -> "Today"
    is MainNavDest.ViewAllHabitsDestination -> "Habits"
    is MainNavDest.ViewAllTasksDestination -> "Tasks"
}

private fun MainNavDest.toIconResId() = when (this) {
    is MainNavDest.AllScheduledTasksDestination -> R.drawable.ic_today
    is MainNavDest.ViewAllHabitsDestination -> R.drawable.ic_habit
    is MainNavDest.ViewAllTasksDestination -> R.drawable.ic_task
}

private fun findMainNavDestByRoute(route: String?): MainNavDest? = when (route) {
    MainNavDest.AllScheduledTasksDestination.route -> MainNavDest.AllScheduledTasksDestination
    MainNavDest.ViewAllHabitsDestination.route -> MainNavDest.ViewAllHabitsDestination
    MainNavDest.ViewAllTasksDestination.route -> MainNavDest.ViewAllTasksDestination
    else -> null
}