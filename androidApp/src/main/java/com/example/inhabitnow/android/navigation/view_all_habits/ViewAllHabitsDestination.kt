package com.example.inhabitnow.android.navigation.view_all_habits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.main.MainNavDest

fun NavGraphBuilder.viewAllHabits() {
    composable(route = MainNavDest.ViewAllHabitsDestination.route) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "ViewAllHabits",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}