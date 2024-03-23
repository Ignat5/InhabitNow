package com.example.inhabitnow.android.navigation.search_tasks

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.search_tasks.SearchTasksScreen
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenNavigation

fun NavGraphBuilder.searchTasksScreen(
    onNavigateToEditTask: (String) -> Unit,
    onBack: () -> Unit
) {
    composable(route = AppNavDest.SearchTasksDestination.route) {
        SearchTasksScreen(
            onNavigation = { destination ->
                when (destination) {
                    is SearchTasksScreenNavigation.EditTask -> {
                        onNavigateToEditTask(destination.taskId)
                    }

                    is SearchTasksScreenNavigation.Back -> onBack()
                }
            }
        )
    }
}