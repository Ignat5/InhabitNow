package com.example.inhabitnow.android.navigation.view_tasks

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.main.MainNavDest
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.ViewTasksScreen
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenNavigation

fun NavGraphBuilder.viewAllTasks(
    onMenuClick: () -> Unit,
    onNavigation: (ViewTasksScreenNavigation) -> Unit
) {
    composable(
        route = MainNavDest.ViewAllTasksDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        ViewTasksScreen(
            onMenuClick = onMenuClick,
            onNavigation = onNavigation
        )
    }
}