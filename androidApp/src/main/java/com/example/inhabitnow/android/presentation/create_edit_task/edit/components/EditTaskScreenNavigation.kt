package com.example.inhabitnow.android.presentation.create_edit_task.edit.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface EditTaskScreenNavigation : ScreenNavigation {
    data class ViewStatistics(val taskId: String) : EditTaskScreenNavigation
    data object Back : EditTaskScreenNavigation
}