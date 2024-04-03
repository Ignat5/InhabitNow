package com.example.inhabitnow.android.navigation.main

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.inhabitnow.android.navigation.view_schedule.viewScheduleScreen
import com.example.inhabitnow.android.navigation.view_habits.viewHabits
import com.example.inhabitnow.android.navigation.view_tasks.viewAllTasks
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.main.MainViewModel
import com.example.inhabitnow.android.presentation.main.components.MainScreenConfig
import com.example.inhabitnow.android.presentation.main.components.MainScreenEvent
import com.example.inhabitnow.android.presentation.main.components.MainScreenNavigation
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressType
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeDialog
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun NavGraphBuilder.mainGraph(
    onNavigateToCreateTask: (taskId: String) -> Unit,
    onNavigateToEditTask: (taskId: String) -> Unit,
    onNavigateToViewStatistics: (taskId: String) -> Unit,
    onNavigateToSearchTasks: () -> Unit
) {
    composable(
        route = AppNavDest.MainGraphDestination.route
    ) {
        val navController = rememberNavController()
        val viewModel: MainViewModel = hiltViewModel()
        BaseScreen(
            viewModel = viewModel,
            onNavigation = { destination ->
                when (destination) {
                    is MainScreenNavigation.CreateTask -> {
                        onNavigateToCreateTask(destination.taskId)
                    }

                    is MainScreenNavigation.SearchTasks -> {
                        onNavigateToSearchTasks()
                    }
                }
            },
            configContent = { config, onEvent ->
                ScreenConfigContent(
                    config = config,
                    onPickTaskTypeResult = { result ->
                        onEvent(MainScreenEvent.ResultEvent.PickTaskType(result))
                    },
                    onPickTaskProgressTypeResult = { result ->
                        onEvent(MainScreenEvent.ResultEvent.PickTaskProgressType(result))
                    }
                )
            },
            screenContent = { _, onEvent ->
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val onNavDestClick = remember {
                    val callback: (MainNavDest) -> Unit = { mainNavDest ->
                        if (currentBackStackEntry?.destination?.route != mainNavDest.route) {
                            navController.navigate(
                                route = mainNavDest.route,
                                navOptions = navOptions {
                                    this.launchSingleTop = true
                                    this.popUpTo(MainNavDest.ViewScheduleDestination.route) {
                                        this.inclusive = false
                                    }
                                }
                            )
                        }
                    }
                    callback
                }
                Scaffold(
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
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = MainNavDest.ViewScheduleDestination.route,
                        route = AppNavDest.MainGraphDestination.route,
                    ) {
                        viewScheduleScreen(
                            onMenuClick = {},
                            onNavigate = { destination ->
                                when (destination) {
                                    is ViewScheduleScreenNavigation.Search -> {
                                        onNavigateToSearchTasks()
                                    }

                                    is ViewScheduleScreenNavigation.EditTask -> {
                                        onNavigateToEditTask(destination.taskId)
                                    }
                                }
                            }
                        )
                        viewHabits(
                            onMenuClick = {},
                            onNavigation = { destination ->
                                when (destination) {
                                    is ViewHabitsScreenNavigation.Base -> {
                                        when (val baseNS = destination.baseNS) {
                                            is BaseViewTasksScreenNavigation.EditTask -> {
                                                onNavigateToEditTask(baseNS.taskId)
                                            }
                                            is BaseViewTasksScreenNavigation.Search -> {
                                                onNavigateToSearchTasks()
                                            }
                                        }
                                    }
                                    is ViewHabitsScreenNavigation.ViewStatistics -> {
                                        onNavigateToViewStatistics(destination.taskId)
                                    }
                                }
                            }
                        )
                        viewAllTasks(
                            onMenuClick = {},
                            onNavigation = { destination ->
                                when (destination) {
                                    is ViewTasksScreenNavigation.Base -> {
                                        when (val baseNS = destination.baseNS) {
                                            is BaseViewTasksScreenNavigation.EditTask -> {
                                                onNavigateToEditTask(baseNS.taskId)
                                            }
                                            is BaseViewTasksScreenNavigation.Search -> {
                                                onNavigateToSearchTasks()
                                            }
                                        }
                                    }

                                }
                            }
                        )
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
    is MainNavDest.ViewScheduleDestination -> "Today"
    is MainNavDest.ViewAllHabitsDestination -> "Habits"
    is MainNavDest.ViewAllTasksDestination -> "Tasks"
}

private fun MainNavDest.toIconResId() = when (this) {
    is MainNavDest.ViewScheduleDestination -> R.drawable.ic_today
    is MainNavDest.ViewAllHabitsDestination -> R.drawable.ic_habit
    is MainNavDest.ViewAllTasksDestination -> R.drawable.ic_task
}

private fun findMainNavDestByRoute(route: String?): MainNavDest? = when (route) {
    MainNavDest.ViewScheduleDestination.route -> MainNavDest.ViewScheduleDestination
    MainNavDest.ViewAllHabitsDestination.route -> MainNavDest.ViewAllHabitsDestination
    MainNavDest.ViewAllTasksDestination.route -> MainNavDest.ViewAllTasksDestination
    else -> null
}