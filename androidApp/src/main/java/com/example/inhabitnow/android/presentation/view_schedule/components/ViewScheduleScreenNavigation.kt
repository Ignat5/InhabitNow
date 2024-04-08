package com.example.inhabitnow.android.presentation.view_schedule.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface ViewScheduleScreenNavigation : ScreenNavigation {
    data object Search : ViewScheduleScreenNavigation
    data class EditTask(val taskId: String) : ViewScheduleScreenNavigation
    data class CreateTask(val taskId: String) : ViewScheduleScreenNavigation
}