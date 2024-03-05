package com.example.inhabitnow.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun RootGraph() {
    val navController = rememberNavController()
    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = "NOT IMPLEMENTED",
            route = AppNavDest.RootGraphDestination.route,
            modifier = Modifier.padding(padding)
        ) {

        }
    }
}