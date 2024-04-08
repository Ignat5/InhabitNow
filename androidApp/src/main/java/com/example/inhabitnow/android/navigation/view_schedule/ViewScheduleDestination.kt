package com.example.inhabitnow.android.navigation.view_schedule

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.root.TargetNavDest
import com.example.inhabitnow.android.presentation.view_schedule.ViewScheduleScreen
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation

fun NavGraphBuilder.viewScheduleScreen(
    onMenuClick: () -> Unit,
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewScheduleDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        ViewScheduleScreen(
            onMenuClick = onMenuClick,
            onNavigate = { destination ->
                when (destination) {
                    is ViewScheduleScreenNavigation.EditTask -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildEditTaskRoute(destination.taskId)
                            )
                        )
                    }

                    is ViewScheduleScreenNavigation.CreateTask -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildCreateTaskRoute(destination.taskId)
                            )
                        )
                    }

                    is ViewScheduleScreenNavigation.Search -> {
                        onNavigate(
                            TargetNavDest.Destination(route = AppNavDest.buildSearchTasksRoute())
                        )
                    }
                }
            }
        )
    }
}