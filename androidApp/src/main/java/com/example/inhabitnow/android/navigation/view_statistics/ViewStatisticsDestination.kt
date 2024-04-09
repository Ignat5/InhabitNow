package com.example.inhabitnow.android.navigation.view_statistics

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.root.TargetNavDest
import com.example.inhabitnow.android.presentation.view_statistics.ViewStatisticsScreen
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenNavigation

fun NavGraphBuilder.viewStatistics(onNavigate: (TargetNavDest) -> Unit) {
    composable(
        route = AppNavDest.ViewStatisticsDestination.route,
        arguments = listOf(
            navArgument(AppNavDest.TASK_ID_KEY) {
                type = NavType.StringType
            }
        )
    ) {
        ViewStatisticsScreen(
            onNavigation = { destination ->
                when (destination) {
                    is ViewStatisticsScreenNavigation.Back -> onNavigate(TargetNavDest.Back)
                }
            }
        )
    }
}