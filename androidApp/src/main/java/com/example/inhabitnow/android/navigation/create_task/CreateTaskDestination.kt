package com.example.inhabitnow.android.navigation.create_task

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.create_task.CreateTaskScreen
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenNavigation

fun NavGraphBuilder.createTaskScreen(onBack: () -> Unit) {
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
                    is CreateTaskScreenNavigation.Back -> onBack()
                }
            }
        )
    }
}