package com.example.inhabitnow.android.navigation.search_tasks

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.search_tasks.SearchTasksScreen
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenNavigation

fun NavGraphBuilder.searchTasksScreen(
    onNavigateToEditTask: (
        taskId: String,
        navOptions: NavOptions
    ) -> Unit,
    onBack: () -> Unit
) {
    composable(route = AppNavDest.SearchTasksDestination.route) {
        SearchTasksScreen(
            onNavigation = { destination ->
                when (destination) {
                    is SearchTasksScreenNavigation.EditTask -> {
                        onNavigateToEditTask(
                            destination.taskId,
                            navOptions {
                                this.popUpTo(
                                    route = AppNavDest.SearchTasksDestination.route,
                                    popUpToBuilder = {
                                        this.inclusive = true
                                    }
                                )
                            }
                        )
                    }

                    is SearchTasksScreenNavigation.Back -> onBack()
                }
            }
        )
    }
}