package com.example.inhabitnow.android.navigation.view_tasks

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.root.TargetNavDest
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.ViewTasksScreen
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenNavigation

fun NavGraphBuilder.viewTasksScreen(
    onMenuClick: () -> Unit,
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewTasksDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        ViewTasksScreen(
            onMenuClick = onMenuClick,
            onNavigation = { destination ->
                when (destination) {
                    is ViewTasksScreenNavigation.Base -> {
                        when (val baseNS = destination.baseNS) {
                            is BaseViewTasksScreenNavigation.EditTask -> {
                                onNavigate(
                                    TargetNavDest.Destination(
                                        route = AppNavDest.buildEditTaskRoute(baseNS.taskId)
                                    )
                                )
                            }

                            is BaseViewTasksScreenNavigation.Search -> {
                                onNavigate(
                                    TargetNavDest.Destination(
                                        route = AppNavDest.buildSearchTasksRoute()
                                    )
                                )
                            }
                        }
                    }

                    is ViewTasksScreenNavigation.CreateTask -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildCreateTaskRoute(destination.taskId)
                            )
                        )
                    }
                }
            }
        )
    }
}