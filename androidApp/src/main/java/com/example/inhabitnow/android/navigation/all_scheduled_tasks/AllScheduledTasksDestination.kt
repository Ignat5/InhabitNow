package com.example.inhabitnow.android.navigation.all_scheduled_tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.main.MainNavDest

fun NavGraphBuilder.allScheduledTasks() {
    composable(route = MainNavDest.AllScheduledTasksDestination.route) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "AllScheduledTasks",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}