package com.example.inhabitnow.android.navigation.root

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.create_task.createTaskScreen
import com.example.inhabitnow.android.navigation.main.mainGraph
import com.example.inhabitnow.android.navigation.view_task_reminders.viewTaskReminders

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun RootGraph() {
    val navController = rememberNavController()
    val onBack = remember(navController) {
        val callback: () -> Unit = {
            navController.popBackStack()
        }
        callback
    }
    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = AppNavDest.MainGraphDestination.route,
            route = AppNavDest.RootGraphDestination.route,
            modifier = Modifier
        ) {
            mainGraph(
                onNavigateToCreateTask = { taskId ->
                    navController.navigate(
                        route = AppNavDest.buildCreateTaskRoute(taskId),
                    )
                }
            )
            createTaskScreen(
                onBack = onBack,
                onNavigateToViewTaskReminders = { taskId ->
                    navController.navigate(
                        route = AppNavDest.buildViewTaskRemindersRoute(taskId)
                    )
                }
            )
            viewTaskReminders(onBack = onBack)
        }
    }
}