package com.example.inhabitnow.android.navigation.view_task_reminders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.view_task_reminders.ViewTaskRemindersScreen
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenNavigation

fun NavGraphBuilder.viewTaskReminders(onBack: () -> Unit) {
    composable(
        route = AppNavDest.ViewTaskReminders.route,
        arguments = listOf(
            navArgument(AppNavDest.TASK_ID_KEY) {
                type = NavType.StringType
            }
        )
    ) {
        ViewTaskRemindersScreen(
            onNavigate = { destination ->
                when (destination) {
                    is ViewTaskRemindersScreenNavigation.Back -> onBack()
                }
            }
        )
    }
}