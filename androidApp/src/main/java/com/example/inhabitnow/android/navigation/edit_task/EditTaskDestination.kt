package com.example.inhabitnow.android.navigation.edit_task

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.edit.EditTaskScreen
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenNavigation

fun NavGraphBuilder.editTaskScreen(
    onViewTaskReminders: (taskId: String) -> Unit,
    onViewTags: () -> Unit,
    onBack: () -> Unit
) {
    composable(
        route = AppNavDest.EditTaskDestination.route,
        arguments = listOf(
            navArgument(AppNavDest.TASK_ID_KEY) {
                type = NavType.StringType
            }
        )
    ) {
        EditTaskScreen(
            onNavigate = { destination ->
                when (destination) {
                    is EditTaskScreenNavigation.Base -> {
                        when (val baseNav = destination.baseNav) {
                            is BaseCreateEditTaskScreenNavigation.ViewTaskReminders -> {
                                onViewTaskReminders(baseNav.taskId)
                            }

                            is BaseCreateEditTaskScreenNavigation.ViewTags -> {
                                onViewTags()
                            }
                        }
                    }

                    is EditTaskScreenNavigation.Back -> onBack()
                    else -> {
                        /* TODO */
                    }
                }
            }
        )
    }
}