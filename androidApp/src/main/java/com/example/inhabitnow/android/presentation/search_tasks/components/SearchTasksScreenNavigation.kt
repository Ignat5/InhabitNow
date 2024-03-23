package com.example.inhabitnow.android.presentation.search_tasks.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface SearchTasksScreenNavigation : ScreenNavigation {
    data class EditTask(val taskId: String) : SearchTasksScreenNavigation
    data object Back : SearchTasksScreenNavigation
}