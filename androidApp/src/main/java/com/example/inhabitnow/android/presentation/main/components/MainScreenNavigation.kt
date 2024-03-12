package com.example.inhabitnow.android.presentation.main.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface MainScreenNavigation: ScreenNavigation {
    data class CreateTask(val taskId: String) : MainScreenNavigation
}