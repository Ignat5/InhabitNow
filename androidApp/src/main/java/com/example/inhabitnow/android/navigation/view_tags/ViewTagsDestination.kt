package com.example.inhabitnow.android.navigation.view_tags

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.view_tags.ViewTagsScreen
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenNavigation

fun NavGraphBuilder.viewTagsScreen(onBack: () -> Unit) {
    composable(route = AppNavDest.ViewTagsDestination.route) {
        ViewTagsScreen(
            onNavigation = { destination ->
                when (destination) {
                    is ViewTagsScreenNavigation.Back -> onBack()
                }
            }
        )
    }
}