package com.example.inhabitnow.android.navigation.view_schedule

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
import com.example.inhabitnow.android.presentation.view_schedule.ViewScheduleScreen
import com.example.inhabitnow.android.presentation.view_schedule.components.ViewScheduleScreenNavigation

fun NavGraphBuilder.viewScheduleScreen(
    onMenuClick: () -> Unit,
    onNavigate: (ViewScheduleScreenNavigation) -> Unit
) {
    composable(
        route = MainNavDest.ViewScheduleDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        ViewScheduleScreen(
            onMenuClick = onMenuClick,
            onNavigate = onNavigate
        )
    }
}