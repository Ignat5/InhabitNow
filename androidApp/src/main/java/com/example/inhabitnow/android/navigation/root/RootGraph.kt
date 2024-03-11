package com.example.inhabitnow.android.navigation.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.navigation.main.mainGraph

@Composable
fun RootGraph() {
    val navController = rememberNavController()
    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = AppNavDest.MainGraphDestination.route,
            route = AppNavDest.RootGraphDestination.route,
            modifier = Modifier.padding(padding)
        ) {
            mainGraph()
        }
    }
}