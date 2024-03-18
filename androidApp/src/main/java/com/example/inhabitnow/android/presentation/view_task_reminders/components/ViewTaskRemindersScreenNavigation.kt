package com.example.inhabitnow.android.presentation.view_task_reminders.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface ViewTaskRemindersScreenNavigation : ScreenNavigation {
    data object Back : ViewTaskRemindersScreenNavigation
}