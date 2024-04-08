package com.example.inhabitnow.android.navigation.view_settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.root.TargetNavDest

fun NavGraphBuilder.viewSettingsScreen(onNavigate: (TargetNavDest) -> Unit) {
    composable(route = AppNavDest.ViewSettingsDestination.route) {
        Box(modifier = Modifier.fillMaxSize()) {
            BackHandler { onNavigate(TargetNavDest.Back) }
            Text(text = "Settings screen")
        }
    }
}