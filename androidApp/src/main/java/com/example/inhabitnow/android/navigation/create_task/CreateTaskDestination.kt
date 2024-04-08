package com.example.inhabitnow.android.navigation.create_task

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.root.TargetNavDest
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.create.CreateTaskScreen
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenNavigation

fun NavGraphBuilder.createTaskScreen(
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.CreateTaskDestination.route,
        arguments = listOf(
            navArgument(AppNavDest.TASK_ID_KEY) {
                type = NavType.StringType
            }
        )
    ) {
        CreateTaskScreen(
            onNavigation = { destination ->
                when (destination) {
                    is CreateTaskScreenNavigation.Back -> {
                        onNavigate(TargetNavDest.Back)
                    }

                    is CreateTaskScreenNavigation.Base -> {
                        when (val sn = destination.baseSN) {
                            is BaseCreateEditTaskScreenNavigation.ViewTaskReminders -> {
                                onNavigate(
                                    TargetNavDest.Destination(
                                        route = AppNavDest.buildViewTaskRemindersRoute(sn.taskId)
                                    )
                                )
                            }

                            is BaseCreateEditTaskScreenNavigation.ViewTags -> {
                                onNavigate(
                                    TargetNavDest.Destination(
                                        route = AppNavDest.buildViewTagsRoute()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}