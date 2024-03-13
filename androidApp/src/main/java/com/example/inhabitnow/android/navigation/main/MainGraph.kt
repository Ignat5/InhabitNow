package com.example.inhabitnow.android.navigation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.main.MainViewModel
import com.example.inhabitnow.android.presentation.main.components.MainScreenConfig
import com.example.inhabitnow.android.presentation.main.components.MainScreenEvent
import com.example.inhabitnow.android.presentation.main.components.MainScreenNavigation
import com.example.inhabitnow.android.presentation.main.components.MainScreenState
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressType
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeDialog
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitnow.android.ui.base.BaseDialog
import com.example.inhabitnow.android.ui.base.BaseDialogActionButtons
import com.example.inhabitnow.android.ui.base.BaseDialogBody
import com.example.inhabitnow.android.ui.base.BaseDialogTitle

fun NavGraphBuilder.mainGraph(
    onNavigateToCreateTask: (taskId: String) -> Unit
) {
    composable(route = AppNavDest.MainGraphDestination.route) {
        val navController = rememberNavController()
        val viewModel: MainViewModel = hiltViewModel()
        BaseScreen(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is MainScreenNavigation.CreateTask -> {
                        onNavigateToCreateTask(destination.taskId)
                    }
                }
            },
            configContent = { config ->
                ScreenConfigContent(
                    config = config,
                    onPickTaskTypeResult = { result ->
                        viewModel.onEvent(MainScreenEvent.ResultEvent.PickTaskType(result))
                    },
                    onPickTaskProgressTypeResult = { result ->
                        viewModel.onEvent(MainScreenEvent.ResultEvent.PickTaskProgressType(result))
                    }
                )
            },
            screenContent = { state, onEvent ->
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val onNavDestClick = remember {
                    val callback: (MainNavDest) -> Unit = { mainNavDest ->
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
                    callback
                }
                Scaffold(
                    topBar = {
                        val titleText = remember(currentBackStackEntry?.destination?.route) {
                            (findMainNavDestByRoute(currentBackStackEntry?.destination?.route)
                                ?: MainNavDest.startDestination).toTitleText()
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
                            onNavDestClick = onNavDestClick
                        )
                    },
                    floatingActionButton = {
                        ScreenFAB(onClick = { onEvent(MainScreenEvent.OnCreateTaskClick) })
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MainNavDest.AllScheduledTasksDestination.route,
                        route = AppNavDest.MainGraphDestination.route,
                        modifier = Modifier.padding(it)
                    ) {
                        allScheduledTasks()
                        viewAllHabits()
                        viewAllTasks()
                    }
                }
            }
        )
    }
}

@Composable
private fun ScreenConfigContent(
    config: MainScreenConfig,
    onPickTaskTypeResult: (PickTaskTypeScreenResult) -> Unit,
    onPickTaskProgressTypeResult: (PickTaskProgressTypeScreenResult) -> Unit
) {
    when (config) {
        is MainScreenConfig.PickTaskType -> {
            PickTaskTypeDialog(
                allTaskTypes = config.allTaskTypes,
                onResult = onPickTaskTypeResult
            )
        }

        is MainScreenConfig.PickTaskProgressType -> {
            PickTaskProgressType(
                allTaskProgressTypes = config.allTaskProgressTypes,
                onResult = onPickTaskProgressTypeResult
            )
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
                currentBackStackEntry?.destination?.hierarchy?.any { it.route == mainNavDest.route }
                    ?: (mainNavDest == MainNavDest.startDestination)

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