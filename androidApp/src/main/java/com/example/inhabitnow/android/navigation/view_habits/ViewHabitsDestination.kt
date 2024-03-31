package com.example.inhabitnow.android.navigation.view_habits

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.main.MainNavDest
import com.example.inhabitnow.android.presentation.view_activities.view_habits.ViewHabitsScreen
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenNavigation

fun NavGraphBuilder.viewHabits(
    onMenuClick: () -> Unit,
    onNavigation: (ViewHabitsScreenNavigation) -> Unit
) {
    composable(
        route = MainNavDest.ViewAllHabitsDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        ViewHabitsScreen(
            onMenuClick = onMenuClick,
            onNavigation = onNavigation
        )
    }
}