package com.example.inhabitnow.android.navigation.edit_task

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.create_edit_task.edit.EditTaskScreen
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenNavigation

fun NavGraphBuilder.editTaskScreen(
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
                    is EditTaskScreenNavigation.Back -> onBack()
                    else -> {
                        /* TODO */
                    }
                }
            }
        )
    }
}