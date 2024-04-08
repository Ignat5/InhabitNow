package com.example.inhabitnow.android.navigation.view_habits

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.root.TargetNavDest
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_habits.ViewHabitsScreen
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenNavigation

fun NavGraphBuilder.viewHabitsScreen(
    onMenuClick: () -> Unit,
    onNavigate: (TargetNavDest) -> Unit
) {
    composable(
        route = AppNavDest.ViewHabitsDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        ViewHabitsScreen(
            onMenuClick = onMenuClick,
            onNavigation = { destination ->
                when (destination) {
                    is ViewHabitsScreenNavigation.Base -> {
                        when (val baseNS = destination.baseNS) {
                            is BaseViewTasksScreenNavigation.EditTask -> {
                                onNavigate(
                                    TargetNavDest.Destination(
                                        route = AppNavDest.buildEditTaskRoute(baseNS.taskId)
                                    )
                                )
                            }

                            is BaseViewTasksScreenNavigation.Search -> {
                                onNavigate(
                                    TargetNavDest.Destination(
                                        route = AppNavDest.buildSearchTasksRoute()
                                    )
                                )
                            }
                        }
                    }

                    is ViewHabitsScreenNavigation.ViewStatistics -> {
                        onNavigate(
                            TargetNavDest.Destination(
                                route = AppNavDest.buildViewStatisticsRoute(destination.taskId)
                            )
                        )
                    }
                }
            }
        )
    }
}