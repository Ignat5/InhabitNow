package com.example.inhabitnow.android.presentation.create_edit_task.base.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface BaseCreateEditTaskScreenNavigation : ScreenNavigation {
    data class ViewTaskReminders(val taskId: String) : BaseCreateEditTaskScreenNavigation
    data object ViewTags : BaseCreateEditTaskScreenNavigation
}