package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

interface CreateTaskScreenNavigation : ScreenNavigation {
    data object Back : CreateTaskScreenNavigation
    data class ViewReminders(val taskId: String) : CreateTaskScreenNavigation
    data object ViewTags : CreateTaskScreenNavigation
}