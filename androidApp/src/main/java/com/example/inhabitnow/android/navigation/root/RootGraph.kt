package com.example.inhabitnow.android.navigation.root

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.create_task.createTaskScreen
import com.example.inhabitnow.android.navigation.edit_task.editTaskScreen
import com.example.inhabitnow.android.navigation.search_tasks.searchTasksScreen
import com.example.inhabitnow.android.navigation.view_habits.viewHabitsScreen
import com.example.inhabitnow.android.navigation.view_schedule.viewScheduleScreen
import com.example.inhabitnow.android.navigation.view_settings.viewSettingsScreen
import com.example.inhabitnow.android.navigation.view_statistics.viewStatistics
import com.example.inhabitnow.android.navigation.view_tags.viewTagsScreen
import com.example.inhabitnow.android.navigation.view_task_reminders.viewTaskRemindersScreen
import com.example.inhabitnow.android.navigation.view_tasks.viewTasksScreen
import kotlinx.coroutines.launch

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun RootGraph() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val rootScope = rememberCoroutineScope()
    RootModalNavigationDrawer(
        currentBackStackEntry = currentBackStackEntry,
        drawerState = drawerState,
        onRootDestinationClick = { rootDestination ->
            rootScope.launch {
                drawerState.close()
                navController.navigate(
                    route = rootDestination.destination.route,
                    navOptions = navOptions {
                        this.launchSingleTop = true
                        this.popUpTo(
                            route = AppNavDest.RootGraphDestination.route,
                            popUpToBuilder = {
                                this.inclusive = false
                            }
                        )
                    }
                )
            }
        }
    ) {
        val onNavigate: (TargetNavDest) -> Unit = remember(navController) {
            val callback: (TargetNavDest) -> Unit = { targetNavDest ->
                when (targetNavDest) {
                    is TargetNavDest.Destination -> {
                        navController.navigate(
                            route = targetNavDest.route,
                            navOptions = targetNavDest.navOptions
                        )
                    }

                    is TargetNavDest.Back -> {
                        navController.popBackStack()
                    }
                }
            }
            callback
        }
        val onMenuClick: () -> Unit = remember(drawerState) {
            val callback: () -> Unit = {
                rootScope.launch {
                    drawerState.open()
                }
            }
            callback
        }
        NavHost(
            navController = navController,
            startDestination = AppNavDest.ViewScheduleDestination.route,
            route = AppNavDest.RootGraphDestination.route,
            modifier = Modifier.fillMaxSize()
        ) {
            viewScheduleScreen(
                onMenuClick = onMenuClick,
                onNavigate = onNavigate
            )
            viewHabitsScreen(
                onMenuClick = onMenuClick,
                onNavigate = onNavigate
            )
            viewTasksScreen(
                onMenuClick = onMenuClick,
                onNavigate = onNavigate
            )
            createTaskScreen(onNavigate)
            viewTaskRemindersScreen(onNavigate)
            viewTagsScreen(onNavigate)
            searchTasksScreen(onNavigate)
            editTaskScreen(onNavigate)
            viewStatistics(onNavigate)
            viewSettingsScreen(onNavigate)
        }
    }
}