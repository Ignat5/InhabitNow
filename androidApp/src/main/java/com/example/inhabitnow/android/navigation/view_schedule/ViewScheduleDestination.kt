package com.example.inhabitnow.android.navigation.view_schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.main.MainNavDest

fun NavGraphBuilder.viewScheduleScreen() {
    composable(route = MainNavDest.ViewScheduleDestination.route) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "ViewSchedule",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}